package com.daicom.daicombackend.orders.dto;

import com.daicom.daicombackend.orders.Order;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderResponse {
    public Long id;
    public String order_number;
    public Integer status;
    public boolean wants_invoice;
    public boolean has_detraccion = false;
    public BigDecimal detraccion_total = null;
    public boolean sent;
    public String created_at;
    public Map<String, Object> client_data;
    public List<Map<String, Object>> certificates;
    public List<Map<String, Object>> payments;
    public Object detraccion = null;
    public List<Map<String, Object>> invoices = new ArrayList<>();
    public Integer order_type;

    public BigDecimal total_facturado;
    public BigDecimal neto_a_cobrar;
    public BigDecimal total_pagado;
    public BigDecimal saldo_pendiente;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.order_number = order.getOrderNumber();
        this.status = order.getStatus();
        this.wants_invoice = order.isWantsInvoice();
        this.sent = order.isSent();
        this.order_type = order.getOrderType();
        this.created_at = order.getCreatedAt() != null ? order.getCreatedAt().toString() : null;

        // Finanzas simuladas (Fake N:M)
        this.total_facturado = order.getTotalFacturado();
        this.total_pagado = order.getTotalPagado();
        this.neto_a_cobrar = this.total_facturado; // Sin detracciones por ahora
        this.saldo_pendiente = this.neto_a_cobrar.subtract(this.total_pagado);

        // Cliente
        this.client_data = new HashMap<>();
        if (order.getClient() != null) {
            this.client_data.put("id", order.getClient().getId());
            this.client_data.put("name", order.getClient().getName());
        }

        // Certificados anidados
        if (order.getCertificates() != null) {
            this.certificates = order.getCertificates().stream().map(c -> {
                Map<String, Object> cMap = new HashMap<>();
                cMap.put("id", c.getId());
                cMap.put("status", c.getStatus());
                cMap.put("equipment", c.getEquipment());
                cMap.put("correlative", c.getCorrelative());
                cMap.put("uuid", c.getUuid());
                
                // --edicion ---
                if (c.getCertificateType() == com.daicom.daicombackend.certificates.CertificateType.ACREDITADO) {
                    cMap.put("certificate_type", 1);
                    cMap.put("certificate_type_label", "ACREDITADO");
                } else if (c.getCertificateType() == com.daicom.daicombackend.certificates.CertificateType.NO_ACREDITADO) {
                    cMap.put("certificate_type", 2);
                    cMap.put("certificate_type_label", "NO ACREDITADO");
                } else {
                    cMap.put("certificate_type", 3);
                    cMap.put("certificate_type_label", "OPERATIVIDAD");
                }

                if (c.getClient() != null) {
                    Map<String, Object> cliMap = new HashMap<>();
                    cliMap.put("id", c.getClient().getId());
                    cliMap.put("name", c.getClient().getName());
                    cMap.put("client_data", cliMap);
                    cMap.put("client", c.getClient().getId());
                }

                if (c.getLab() != null) {
                    Map<String, Object> lMap = new HashMap<>();
                    lMap.put("id", c.getLab().getId());
                    lMap.put("name", c.getLab().getName());
                    cMap.put("lab_data", lMap);
                    cMap.put("lab", c.getLab().getId());
                }
                
                cMap.put("emission_date", c.getEmissionDate() != null ? c.getEmissionDate().toString() : "");
                
                cMap.put("uploaded_xls", c.getUploadedXls() != null && !c.getUploadedXls().isEmpty());
                cMap.put("attached_pdf", c.getAttachedPdf());
                cMap.put("uploaded", c.getAttachedPdf() != null && !c.getAttachedPdf().isEmpty());
                
                // Generar registro con año
                if (c.getEmissionDate() != null) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy");
                    cMap.put("registry_code", String.format("%s-%08d", sdf.format(c.getEmissionDate()), c.getCorrelative() != null ? c.getCorrelative() : 0));
                } else {
                    cMap.put("registry_code", String.format("20??-%08d", c.getCorrelative() != null ? c.getCorrelative() : 0));
                }
                
                return cMap;
            }).collect(Collectors.toList());
        } else {
            this.certificates = new ArrayList<>();
        }

        // Facturas anidadas (snake_case para el frontend: order summary, ícono, etc.)
        if (order.getInvoices() != null) {
            this.invoices = order.getInvoices().stream().map(inv -> {
                Map<String, Object> iMap = new HashMap<>();
                iMap.put("id", inv.getId());
                iMap.put("invoice_number", inv.getInvoiceNumber());
                iMap.put("invoice_date", inv.getInvoiceDate() != null
                        ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(inv.getInvoiceDate()) : null);
                iMap.put("amount", inv.getAmount());
                iMap.put("pdf_url", inv.getPdf());
                return iMap;
            }).collect(Collectors.toList());
        }

        // Pagos anidados
        if (order.getPayments() != null) {
            this.payments = order.getPayments().stream().map(p -> {
                Map<String, Object> pMap = new HashMap<>();
                pMap.put("id", p.getId());
                pMap.put("amount", p.getAmount() != null ? p.getAmount().toString() : "0");
                pMap.put("payment_method", p.getPaymentMethod().name());
                pMap.put("payment_date", p.getPaymentDate() != null ? p.getPaymentDate().toString() : "");
                pMap.put("notes", p.getNotes());
                pMap.put("payment_proof", p.getPaymentProof());
                return pMap;
            }).collect(Collectors.toList());
        } else {
            this.payments = new ArrayList<>();
        }
    }
}