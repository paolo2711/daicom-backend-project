package com.daicom.daicombackend.certificates;

import com.daicom.daicombackend.clients.Client;
import com.daicom.daicombackend.company.Company;
import com.daicom.daicombackend.labs.Lab;
import com.daicom.daicombackend.orders.Order;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "certificates")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = true)
    @JoinColumn(name = "order_id", nullable = true)
    private Order order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CertificateType certificateType;

    @Column(nullable = false)
    private Long correlative;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date emissionDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // campos extra del certificado
    @Column(nullable = false)
    private String equipment = "";

    @Column(nullable = false)
    private Integer status = 1;

    @Column(length = 36)
    private String uuid = UUID.randomUUID().toString();

    @Column(name = "signature_requested", nullable = false)
    private boolean signatureRequested = false;

    @Column(name = "uploaded_xls")
    private String uploadedXls = "";

    @Column(name = "attached_pdf")
    private String attachedPdf = "";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }
    public Lab getLab() { return lab; }
    public void setLab(Lab lab) { this.lab = lab; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public CertificateType getCertificateType() { return certificateType; }
    public void setCertificateType(CertificateType certificateType) { this.certificateType = certificateType; }
    public Long getCorrelative() { return correlative; }
    public void setCorrelative(Long correlative) { this.correlative = correlative; }
    public Date getEmissionDate() { return emissionDate; }
    public void setEmissionDate(Date emissionDate) { this.emissionDate = emissionDate; }
    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
    public String getEquipment() {
        return equipment;
    }
    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public boolean isSignatureRequested() {
        return signatureRequested;
    }
    public void setSignatureRequested(boolean signatureRequested) {
        this.signatureRequested = signatureRequested;
    }
    public String getUploadedXls() {
        return uploadedXls;
    }
    public void setUploadedXls(String uploadedXls) {
        this.uploadedXls = uploadedXls;
    }
    public String getAttachedPdf() {
        return attachedPdf;
    }
    public void setAttachedPdf(String attachedPdf) {
        this.attachedPdf = attachedPdf;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}