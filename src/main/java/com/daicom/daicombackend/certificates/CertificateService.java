package com.daicom.daicombackend.certificates;

import com.daicom.daicombackend.auth.User;
import com.daicom.daicombackend.auth.UserRepository;
import com.daicom.daicombackend.certificates.dto.CertificateRequest;
import com.daicom.daicombackend.certificates.dto.CertificateResponse;
import com.daicom.daicombackend.clients.Client;
import com.daicom.daicombackend.clients.ClientRepository;
import com.daicom.daicombackend.common.audit.AuditService;
import com.daicom.daicombackend.company.Company;
import com.daicom.daicombackend.company.CompanyRepository;
import com.daicom.daicombackend.labs.Lab;
import com.daicom.daicombackend.labs.LabRepository;
import com.daicom.daicombackend.orders.Order;
import com.daicom.daicombackend.orders.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final OrderRepository orderRepository;
    private final LabRepository labRepository;
    private final ClientRepository clientRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final AuditService auditService;
    private final PdfQrStamper pdfQrStamper;

    // carpeta que se sirve en /media/**
    private static final String MEDIA_ROOT = "uploads";

    public CertificateService(CertificateRepository certificateRepository, OrderRepository orderRepository,
                              LabRepository labRepository, ClientRepository clientRepository,
                              CompanyRepository companyRepository, UserRepository userRepository,
                              AuditService auditService, PdfQrStamper pdfQrStamper) {
        this.certificateRepository = certificateRepository;
        this.orderRepository = orderRepository;
        this.labRepository = labRepository;
        this.clientRepository = clientRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.auditService = auditService;
        this.pdfQrStamper = pdfQrStamper;
    }

    private Company getMainCompany() {
        return companyRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Error: No existe compañía configurada."));
    }

    public java.util.Map<String, Long> getNextCorrelative(int correlativeType) {
        Company company = getMainCompany();
        Long nextValue;
        
        if (correlativeType == 1) {
            nextValue = company.getAccreditedCorrelative();
        } else if (correlativeType == 2) {
            nextValue = company.getNonAccreditedCorrelative();
        } else if (correlativeType == 3) {
            nextValue = company.getOperationalCorrelative();
        } else {
            throw new RuntimeException("Tipo de correlativo inválido. Use 1, 2 o 3.");
        }
        
        java.util.Map<String, Long> response = new java.util.HashMap<>();
        response.put("correlative", nextValue);
        return response;
    }

    @Transactional
    public CertificateResponse create(CertificateRequest request, String currentUsername) {
        Order order = null;
        if (request.getOrderId() != null) {
            order = orderRepository.findById(request.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Orden no encontrada."));
        }
        Lab lab = labRepository.findById(request.getLabId())
                .orElseThrow(() -> new RuntimeException("Laboratorio no encontrado."));
        Client client = clientRepository.findByIdAndDeletedFalse(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado."));
        Company company = getMainCompany();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        Certificate certificate = new Certificate();
        certificate.setOrder(order);
        certificate.setLab(lab);
        certificate.setClient(client);
        certificate.setCertificateType(request.getCertificateType());
        certificate.setEmissionDate(request.getEmissionDate());
        certificate.setCompany(company);
        
        certificate.setEquipment(request.getEquipment() != null ? request.getEquipment() : "");
        certificate.setSignatureRequested(request.getSignatureRequested() != null ? request.getSignatureRequested() : false);

        Long currentCorrelative;
        if (request.getCertificateType() == CertificateType.ACREDITADO) {
            currentCorrelative = company.getAccreditedCorrelative();
            company.setAccreditedCorrelative(currentCorrelative + 1);
        } else if (request.getCertificateType() == CertificateType.NO_ACREDITADO) {
            currentCorrelative = company.getNonAccreditedCorrelative();
            company.setNonAccreditedCorrelative(currentCorrelative + 1);
        } else { // OPERATIVIDAD
            currentCorrelative = company.getOperationalCorrelative();
            company.setOperationalCorrelative(currentCorrelative + 1);
        }
        
        certificate.setCorrelative(currentCorrelative);

        companyRepository.save(company);
        Certificate savedCertificate = certificateRepository.save(certificate);

        // auditoria
        auditService.registrar(currentUser, "CREATE_CERTIFICATE", "Certificate ID: " + savedCertificate.getId());

        return new CertificateResponse(savedCertificate);
    }

    public List<CertificateResponse> findAll() {
        return certificateRepository.findAll().stream()
                .map(CertificateResponse::new)
                .collect(Collectors.toList());
    }

    public CertificateResponse findById(Long id) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificado no encontrado."));
        return new CertificateResponse(certificate);
    }

    // Desvincular, Vincular y Anular 
    @Transactional
    public CertificateResponse patch(Long id, java.util.Map<String, Object> updates) {
        Certificate cert = certificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificado no encontrado."));

        if (updates.containsKey("order")) {
            Object orderIdObj = updates.get("order");
            if (orderIdObj == null) {
                cert.setOrder(null); // Desvincular
            } else {
                Long orderId = Long.valueOf(orderIdObj.toString());
                Order order = orderRepository.findById(orderId).orElse(null);
                cert.setOrder(order); 
            }
        }
        
        if (updates.containsKey("status")) {
            cert.setStatus(Integer.valueOf(updates.get("status").toString())); 
        }

        return new CertificateResponse(certificateRepository.save(cert));
    }

    // --- CARGA MANUAL DEL PDF BASE (antes "subir Excel") ---
    @Transactional
    public CertificateResponse uploadBase(Long id, MultipartFile file) {
        Certificate cert = certificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificado no encontrado."));
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("No se recibió ningún archivo.");
        }
        String relativePath = saveFile(file, "bases");
        cert.setUploadedXls(relativePath);
        return new CertificateResponse(certificateRepository.save(cert));
    }

    // --- ESTAMPAR QR (contenido = UUID del certificado) SOBRE EL PDF BASE ---
    @Transactional
    public CertificateResponse attachQr(Long id) {
        Certificate cert = certificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificado no encontrado."));

        if (cert.getUploadedXls() == null || cert.getUploadedXls().isEmpty()) {
            throw new RuntimeException("El certificado no tiene un PDF base para estampar el QR.");
        }

        File sourcePdf = Paths.get(MEDIA_ROOT, cert.getUploadedXls()).toFile();
        if (!sourcePdf.exists()) {
            throw new RuntimeException("No se encontró el PDF base en el servidor.");
        }

        try {
            Path signedDir = Paths.get(MEDIA_ROOT, "signed");
            Files.createDirectories(signedDir);
            String fileName = UUID.randomUUID() + "_qr.pdf";
            File outputPdf = signedDir.resolve(fileName).toFile();

            pdfQrStamper.stampQr(sourcePdf, outputPdf, cert.getUuid());

            cert.setAttachedPdf("signed/" + fileName);
            return new CertificateResponse(certificateRepository.save(cert));
        } catch (Exception e) {
            throw new RuntimeException("Error al estampar el QR: " + e.getMessage());
        }
    }

    // Guarda un archivo bajo uploads/<subfolder>/ y devuelve la ruta relativa a MEDIA_ROOT
    private String saveFile(MultipartFile file, String subfolder) {
        try {
            Path dir = Paths.get(MEDIA_ROOT, subfolder);
            Files.createDirectories(dir);
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path target = dir.resolve(fileName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return subfolder + "/" + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el archivo: " + e.getMessage());
        }
    }

    // Editar
    @Transactional
    public CertificateResponse update(Long id, CertificateRequest request) {
        Certificate cert = certificateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Certificado no encontrado."));

        if (request.getEquipment() != null) cert.setEquipment(request.getEquipment());
        if (request.getSignatureRequested() != null) cert.setSignatureRequested(request.getSignatureRequested());
        if (request.getEmissionDate() != null) cert.setEmissionDate(request.getEmissionDate());
        if (request.getCertificateType() != null) cert.setCertificateType(request.getCertificateType());

        if (request.getClientId() != null) {
            Client client = clientRepository.findByIdAndDeletedFalse(request.getClientId()).orElse(null);
            cert.setClient(client);
        }
        
        if (request.getLabId() != null) {
            Lab lab = labRepository.findById(request.getLabId()).orElse(null);
            cert.setLab(lab);
        }

        return new CertificateResponse(certificateRepository.save(cert));
    }

}