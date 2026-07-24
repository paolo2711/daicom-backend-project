package com.daicom.daicombackend.certificates.dto;

import com.daicom.daicombackend.certificates.CertificateType;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

public class CertificateRequest {

    private Long orderId;

    @NotNull(message = "El ID del laboratorio es obligatorio")
    private Long labId;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long clientId;

    @NotNull(message = "El tipo de certificado es obligatorio")
    private CertificateType certificateType;

    @NotNull(message = "La fecha de emisión es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date emissionDate;

    private String equipment;
    private Boolean signatureRequested;

    // Getters y Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Long getLabId() { return labId; }
    public void setLabId(Long labId) { this.labId = labId; }
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public CertificateType getCertificateType() { return certificateType; }
    public void setCertificateType(CertificateType certificateType) { this.certificateType = certificateType; }
    public Date getEmissionDate() { return emissionDate; }
    public void setEmissionDate(Date emissionDate) { this.emissionDate = emissionDate; }

    // --- Adaptadores para capturar FormData (snake_case) del Frontend ---
    public void setClient(Long client) { this.clientId = client; }
    public void setLab(Long lab) { this.labId = lab; }
    // vincula el cert a una orden (equipo extra nuevo)
    public void setOrder(Long order) { this.orderId = order; }
    public void setEmission_date(String emission_date) {
        try {
            this.emissionDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(emission_date);
        } catch (java.text.ParseException e) {
            this.emissionDate = null;
        }
    }
    public void setCertificate_type(int type) {
        if (type == 1) this.certificateType = CertificateType.ACREDITADO;
        else if (type == 2) this.certificateType = CertificateType.NO_ACREDITADO;
        else this.certificateType = CertificateType.OPERATIVIDAD;
    }
    public String getEquipment() { return equipment; }
    public void setEquipment(String equipment) { this.equipment = equipment; }
    public Boolean getSignatureRequested() { return signatureRequested; }
    public void setSignatureRequested(Boolean signatureRequested) { this.signatureRequested = signatureRequested; }
    
    // Adaptador para atrapar snake_case de Vue
    public void setSignature_requested(Boolean signature_requested) { this.signatureRequested = signature_requested; }
}