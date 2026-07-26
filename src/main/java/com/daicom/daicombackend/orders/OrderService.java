package com.daicom.daicombackend.orders;

import com.daicom.daicombackend.clients.Client;
import com.daicom.daicombackend.clients.ClientRepository;
import com.daicom.daicombackend.company.Company;
import com.daicom.daicombackend.company.CompanyRepository;
import com.daicom.daicombackend.orders.dto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.daicom.daicombackend.common.audit.AuditService;
import com.daicom.daicombackend.auth.User;
import com.daicom.daicombackend.auth.UserRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderInvoiceRepository invoiceRepository;
    private final OrderPaymentRepository paymentRepository;
    private final ClientRepository clientRepository;
    private final CompanyRepository companyRepository;
    private final AuditService auditService;
    private final UserRepository userRepository;
    // Repositorios para los certificados
    private final com.daicom.daicombackend.certificates.CertificateRepository certificateRepository;
    private final com.daicom.daicombackend.labs.LabRepository labRepository;
    private final com.daicom.daicombackend.reports.OrderExcelExporter orderExcelExporter;

    public OrderService(OrderRepository orderRepository, OrderInvoiceRepository invoiceRepository,
                        OrderPaymentRepository paymentRepository, ClientRepository clientRepository,
                        CompanyRepository companyRepository, AuditService auditService, UserRepository userRepository,
                        com.daicom.daicombackend.certificates.CertificateRepository certificateRepository,
                        com.daicom.daicombackend.labs.LabRepository labRepository,
                        com.daicom.daicombackend.reports.OrderExcelExporter orderExcelExporter) {
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentRepository = paymentRepository;
        this.clientRepository = clientRepository;
        this.companyRepository = companyRepository;
        this.auditService = auditService;
        this.userRepository = userRepository;
        this.certificateRepository = certificateRepository;
        this.labRepository = labRepository;
        this.orderExcelExporter = orderExcelExporter;
    }

    private Company getMainCompany() {
        return companyRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Error: No existe compañía configurada."));
    }

    public java.util.Map<String, Object> getNextOrderCorrelative() {
        Company company = getMainCompany();
        Long nextValue = company.getServiceOrderCorrelative();
        if (nextValue == null) nextValue = 1L;
        
        int currentYear = java.time.Year.now().getValue();
        String preview = String.format("OS-%d-%05d", currentYear, nextValue);
        
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("correlative", nextValue);
        response.put("preview", preview);
        return response;
    }

    // --- LÓGICA REAL DE GUARDADO DE ARCHIVOS ---
    private String saveFile(MultipartFile file, String subfolder) {
        if (file == null || file.isEmpty()) return null;
        try {
            String uploadDir = "uploads/" + subfolder + "/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // Generar nombre único para evitar colisiones
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo físicamente: " + e.getMessage());
        }
    }

    // --- LOGICA DE ESTADO FINANCIERO (Sección 4.5) ---
    public void recalculateOrderStatus(Order order) {
        BigDecimal totalFacturado = order.getTotalFacturado();
        BigDecimal totalPagado = order.getTotalPagado();
        BigDecimal saldoPendiente = order.getSaldoPendiente();

        if (!order.isWantsInvoice()) {
            if (totalPagado.compareTo(BigDecimal.ZERO) > 0) {
                order.setStatus(5); // PAID
            } else {
                order.setStatus(1); // EN_PROCESO
            }
        } else {
            if (totalFacturado.compareTo(BigDecimal.ZERO) > 0) {
                if (saldoPendiente.compareTo(BigDecimal.ZERO) <= 0) {
                    order.setStatus(5); // PAID
                } else {
                    order.setStatus(2); // DEUDA
                }
            } else {
                if (totalPagado.compareTo(BigDecimal.ZERO) > 0) {
                    order.setStatus(3); // ABONADO
                } else {
                    order.setStatus(1); // EN_PROCESO
                }
            }
        }
        orderRepository.save(order);
    }

    // --- CRUD ORDENES ---
    @Transactional
    public OrderResponse createOrder(OrderRequest request, String currentUsername) {
        Client client = clientRepository.findByIdAndDeletedFalse(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado."));

        Company company = getMainCompany();

        // 1. Lógica de correlativo automático de Órdenes (Sección 5.3)
        int currentYear = java.time.Year.now().getValue();
        Long currentCorrelative = company.getServiceOrderCorrelative();
        
        // Formato: OS-2026-00001
        String generatedOrderNumber = String.format("OS-%d-%05d", currentYear, currentCorrelative);
        
        // 2. Incrementar y guardar en Company
        company.setServiceOrderCorrelative(currentCorrelative + 1);
        companyRepository.save(company);

        // 3. Crear la orden
        Order order = new Order();
        order.setOrderNumber(generatedOrderNumber); // Asignado por el backend
        order.setClient(client);
        order.setWantsInvoice(request.isWantsInvoice());
        order.setCompany(company);
        
        recalculateOrderStatus(order); 
        Order savedOrder = orderRepository.save(order);

        // crea los certificados que vienen en la orden
        if (request.getOrderType() != null && request.getOrderType() == 1 && request.getItems() != null) {
            java.util.Date today = new java.util.Date();

            for (java.util.Map<String, Object> item : request.getItems()) {
                String modo = (String) item.get("modo");

                if ("existente".equals(modo)) {
                    // Vincular equipo huérfano existente
                    Long certId = Long.valueOf(item.get("cert_id").toString());
                    com.daicom.daicombackend.certificates.Certificate cert = certificateRepository.findById(certId).orElse(null);
                    if (cert != null) {
                        cert.setOrder(savedOrder);
                        cert.setClient(client);
                        certificateRepository.save(cert);
                        savedOrder.getCertificates().add(cert); 
                    }
                } else if ("nuevo".equals(modo)) {
                    // Crear certificado en lote desde cero
                    int certTypeValue = Integer.parseInt(item.get("certificate_type").toString());
                    com.daicom.daicombackend.certificates.CertificateType certTypeEnum;
                    Long correlative;

                    if (certTypeValue == 1) {
                        certTypeEnum = com.daicom.daicombackend.certificates.CertificateType.ACREDITADO;
                        correlative = company.getAccreditedCorrelative();
                        company.setAccreditedCorrelative(correlative + 1);
                    } else if (certTypeValue == 2) {
                        certTypeEnum = com.daicom.daicombackend.certificates.CertificateType.NO_ACREDITADO;
                        correlative = company.getNonAccreditedCorrelative();
                        company.setNonAccreditedCorrelative(correlative + 1);
                    } else {
                        certTypeEnum = com.daicom.daicombackend.certificates.CertificateType.OPERATIVIDAD;
                        correlative = company.getOperationalCorrelative();
                        company.setOperationalCorrelative(correlative + 1);
                    }

                    Long labId = Long.valueOf(item.get("lab").toString());
                    com.daicom.daicombackend.labs.Lab lab = labRepository.findById(labId).orElse(null);

                    com.daicom.daicombackend.certificates.Certificate newCert = new com.daicom.daicombackend.certificates.Certificate();
                    newCert.setOrder(savedOrder);
                    newCert.setClient(client);
                    newCert.setLab(lab);
                    newCert.setCertificateType(certTypeEnum);
                    newCert.setEquipment(item.get("name") != null ? item.get("name").toString() : "");
                    newCert.setEmissionDate(today);
                    newCert.setCorrelative(correlative);
                    newCert.setCompany(company);
                    newCert.setStatus(1); 
                    
                    certificateRepository.save(newCert);
                    savedOrder.getCertificates().add(newCert); 
                }
            }
            // Guardar los nuevos correlativos gastados de la empresa
            companyRepository.save(company);
        }

        // 4. Registrar auditoría
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        auditService.registrar(currentUser, "CREATE_ORDER", "Order Number: " + savedOrder.getOrderNumber());

        return new OrderResponse(savedOrder);
    }

    public List<OrderResponse> findAllOrders() {
        return orderRepository.findAll().stream().map(OrderResponse::new).collect(Collectors.toList());
    }

   
    public byte[] exportOrdersToExcel(List<Long> ids) {
        List<Order> orders;
        if (ids == null || ids.isEmpty()) {
            orders = orderRepository.findAll();
        } else {
            java.util.Map<Long, Order> porId = orderRepository.findAllById(ids).stream()
                    .collect(Collectors.toMap(Order::getId, o -> o));
            orders = ids.stream().map(porId::get).filter(java.util.Objects::nonNull).collect(Collectors.toList());
        }
        return orderExcelExporter.build(orders);
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        return new OrderResponse(order);
    }

    @Transactional
    public OrderResponse updateOrder(Long id, OrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        Client client = clientRepository.findByIdAndDeletedFalse(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        order.setClient(client);
        order.setWantsInvoice(request.isWantsInvoice());
        
        recalculateOrderStatus(order);
        return new OrderResponse(orderRepository.save(order));
    }

    // Edición parcial: reasigna el cliente de la orden y, opcionalmente,
    // sincroniza ese mismo cliente en los certificados seleccionados (checkboxes
    // "Sincronizar dueño de Equipos" del frontend).
    @Transactional
    public OrderResponse syncOrderCertificates(Long id, OrderRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        Client client = clientRepository.findByIdAndDeletedFalse(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        order.setClient(client);

        String syncCertificates = request.getSyncCertificates();
        if (syncCertificates != null && !syncCertificates.isBlank()) {
            for (String rawId : syncCertificates.split(",")) {
                rawId = rawId.trim();
                if (rawId.isEmpty()) continue;
                certificateRepository.findById(Long.valueOf(rawId)).ifPresent(cert -> {
                    cert.setClient(client);
                    certificateRepository.save(cert);
                });
            }
        }

        recalculateOrderStatus(order);
        return new OrderResponse(orderRepository.save(order));
    }

    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Orden no encontrada");
        }
        orderRepository.deleteById(id);
    }

    // --- CRUD DE FACTURAS (OrderInvoice) ---
    public List<OrderInvoiceResponse> getInvoicesByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        return order.getInvoices().stream()
                .map(OrderInvoiceResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderInvoiceResponse addInvoice(Long orderId, OrderInvoiceRequest request) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        OrderInvoice invoice = new OrderInvoice();
        invoice.setOrder(order);
        invoice.setInvoiceNumber(request.getInvoiceNumber());
        invoice.setInvoiceDate(request.getInvoiceDate());
        invoice.setAmount(request.getAmount());

        // Guardado físico real
        String filePath = saveFile(request.getFile(), "invoices");
        if (filePath != null) {
            invoice.setPdf(filePath);
        }

        order.getInvoices().add(invoice);
        OrderInvoice savedInvoice = invoiceRepository.save(invoice);
        recalculateOrderStatus(order);
        return new OrderInvoiceResponse(savedInvoice);
    }

    @Transactional
    public OrderInvoiceResponse updateInvoice(Long id, OrderInvoiceRequest request) {
        OrderInvoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        invoice.setInvoiceNumber(request.getInvoiceNumber());
        invoice.setInvoiceDate(request.getInvoiceDate());
        invoice.setAmount(request.getAmount());

        if (request.getFile() != null && !request.getFile().isEmpty()) {
            String filePath = saveFile(request.getFile(), "invoices");
            invoice.setPdf(filePath);
        }

        OrderInvoice updatedInvoice = invoiceRepository.save(invoice);
        recalculateOrderStatus(invoice.getOrder());
        return new OrderInvoiceResponse(updatedInvoice);
    }

    @Transactional
    public void deleteInvoice(Long id) {
        OrderInvoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        Order order = invoice.getOrder();
        order.getInvoices().remove(invoice);
        invoiceRepository.delete(invoice);
        recalculateOrderStatus(order);
    }

    // --- CRUD DE PAGOS (OrderPayment) ---
    @Transactional
    public OrderPaymentResponse addPayment(Long orderId, OrderPaymentRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        OrderPayment payment = new OrderPayment();
        payment.setOrder(order);
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentDate(request.getPaymentDate());
        payment.setNotes(request.getNotes());

        // Guardado físico real
        String filePath = saveFile(request.getFile(), "payments");
        if (filePath != null) {
            payment.setPaymentProof(filePath);
        }

        order.getPayments().add(payment);
        OrderPayment savedPayment = paymentRepository.save(payment);
        recalculateOrderStatus(order);
        return new OrderPaymentResponse(savedPayment);
    }

    @Transactional
    public OrderPaymentResponse updatePayment(Long id, OrderPaymentRequest request) {
        OrderPayment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentDate(request.getPaymentDate());
        payment.setNotes(request.getNotes());

        if (request.getFile() != null && !request.getFile().isEmpty()) {
            String filePath = saveFile(request.getFile(), "payments");
            payment.setPaymentProof(filePath);
        }

        OrderPayment updatedPayment = paymentRepository.save(payment);
        recalculateOrderStatus(payment.getOrder());
        return new OrderPaymentResponse(updatedPayment);
    }

    @Transactional
    public void deletePayment(Long id) {
        OrderPayment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
        Order order = payment.getOrder();
        order.getPayments().remove(payment);
        paymentRepository.delete(payment);
        recalculateOrderStatus(order);
    }
}