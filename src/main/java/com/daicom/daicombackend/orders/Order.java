package com.daicom.daicombackend.orders;

import com.daicom.daicombackend.clients.Client;
import com.daicom.daicombackend.company.Company;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false)
    private Integer status = 1;

    @Column(name = "order_type", nullable = false)
    private Integer orderType = 1;

    @Column(nullable = false)
    private boolean wantsInvoice = true;

    @Column(nullable = false)
    private boolean sent = false;

    @Column(nullable = false)
    private boolean deleted = false;

    // Relación bidireccional 
    @OneToMany(mappedBy = "order")
    private List<com.daicom.daicombackend.certificates.Certificate> certificates = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderInvoice> invoices = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderPayment> payments = new ArrayList<>();

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); this.updatedAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    // Propiedades Calculadas (no persistidas en DB)
    @Transient
    public BigDecimal getTotalFacturado() {
        return invoices.stream()
                .map(OrderInvoice::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transient
    public BigDecimal getTotalPagado() {
        return payments.stream()
                .map(OrderPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transient
    public BigDecimal getSaldoPendiente() {
        return getTotalFacturado().subtract(getTotalPagado());
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getOrderType() { return orderType; }
    public void setOrderType(Integer orderType) { this.orderType = orderType; }
    public boolean isWantsInvoice() { return wantsInvoice; }
    public void setWantsInvoice(boolean wantsInvoice) { this.wantsInvoice = wantsInvoice; }
    public boolean isSent() { return sent; }
    public void setSent(boolean sent) { this.sent = sent; }
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<OrderInvoice> getInvoices() { return invoices; }
    public void setInvoices(List<OrderInvoice> invoices) { this.invoices = invoices; }
    public List<OrderPayment> getPayments() { return payments; }
    public void setPayments(List<OrderPayment> payments) { this.payments = payments; }
    public List<com.daicom.daicombackend.certificates.Certificate> getCertificates() { return certificates; }
    public void setCertificates(List<com.daicom.daicombackend.certificates.Certificate> certificates) { this.certificates = certificates; }
}