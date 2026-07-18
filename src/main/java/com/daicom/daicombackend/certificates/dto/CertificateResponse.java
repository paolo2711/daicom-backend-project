package com.daicom.daicombackend.certificates.dto;

import com.daicom.daicombackend.certificates.Certificate;
import com.daicom.daicombackend.certificates.CertificateType;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CertificateResponse {

    public Long id;
    public String registry_code;
    public Integer certificate_type;
    public String certificate_type_label;
    public Long client;
    public Map<String, Object> client_data;
    public Long lab;
    public Map<String, Object> lab_data;
    public String equipment;
    public Integer status;
    public String uuid;
    public boolean signature_requested;
    public String uploaded_xls;
    public String attached_pdf;
    public String created_at;
    public String updated_at;
    public String order_number;
    public Integer order_status;
    public boolean order_has_invoices;
    public boolean order_has_payments;
    public Long correlative;
    public Date emission_date;

    public CertificateResponse(Certificate certificate) {
        this.id = certificate.getId();
        this.correlative = certificate.getCorrelative();
        this.emission_date = certificate.getEmissionDate();
        this.equipment = certificate.getEquipment();
        this.status = certificate.getStatus();
        this.uuid = certificate.getUuid();
        this.signature_requested = certificate.isSignatureRequested();
        this.uploaded_xls = certificate.getUploadedXls();
        this.attached_pdf = certificate.getAttachedPdf();
        this.created_at = certificate.getCreatedAt() != null ? certificate.getCreatedAt().toString() : null;
        this.updated_at = certificate.getUpdatedAt() != null ? certificate.getUpdatedAt().toString() : null;

        if (certificate.getEmissionDate() != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy");
            String year = sdf.format(certificate.getEmissionDate());
            this.registry_code = String.format("%s-%08d", year, certificate.getCorrelative());
        } else {
            this.registry_code = String.format("20??-%08d", certificate.getCorrelative());
        }

        if (certificate.getCertificateType() == CertificateType.ACREDITADO) {
            this.certificate_type = 1;
            this.certificate_type_label = "ACREDITADO";
        } else if (certificate.getCertificateType() == CertificateType.NO_ACREDITADO) {
            this.certificate_type = 2;
            this.certificate_type_label = "NO ACREDITADO";
        } else {
            this.certificate_type = 3;
            this.certificate_type_label = "OPERATIVIDAD";
        }

        this.client_data = new HashMap<>();
        if (certificate.getClient() != null) {
            this.client = certificate.getClient().getId();
            this.client_data.put("id", certificate.getClient().getId());
            this.client_data.put("name", certificate.getClient().getName());
        }

        this.lab_data = new HashMap<>();
        if (certificate.getLab() != null) {
            this.lab = certificate.getLab().getId();
            this.lab_data.put("id", certificate.getLab().getId());
            this.lab_data.put("name", certificate.getLab().getName()); 
            this.lab_data.put("code", certificate.getLab().getName()); 
        }

        if (certificate.getOrder() != null) {
            this.order_number = certificate.getOrder().getOrderNumber();
            this.order_status = 1;
            this.order_has_invoices = !certificate.getOrder().getInvoices().isEmpty() || !certificate.getOrder().isWantsInvoice();
            this.order_has_payments = !certificate.getOrder().getPayments().isEmpty();
        } else {
            this.order_number = null;
            this.order_status = 1;
            this.order_has_invoices = false;
            this.order_has_payments = false;
        }
    }
}