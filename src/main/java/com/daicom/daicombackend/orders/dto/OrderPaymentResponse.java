package com.daicom.daicombackend.orders.dto;

import com.daicom.daicombackend.orders.OrderPayment;
import java.math.BigDecimal;
import java.util.Date;

public class OrderPaymentResponse {
    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private String paymentMethod;
    private Date paymentDate;
    private String paymentProof;
    private String notes;

    public OrderPaymentResponse(OrderPayment payment) {
        this.id = payment.getId();
        this.orderId = payment.getOrder().getId();
        this.amount = payment.getAmount();
        this.paymentMethod = payment.getPaymentMethod().name();
        this.paymentDate = payment.getPaymentDate();
        this.paymentProof = payment.getPaymentProof();
        this.notes = payment.getNotes();
    }

    // Getters
    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public BigDecimal getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public Date getPaymentDate() { return paymentDate; }
    public String getPaymentProof() { return paymentProof; }
    public String getNotes() { return notes; }
}