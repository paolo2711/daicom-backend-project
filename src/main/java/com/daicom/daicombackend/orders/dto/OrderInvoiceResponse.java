package com.daicom.daicombackend.orders.dto;

import com.daicom.daicombackend.orders.OrderInvoice;
import java.math.BigDecimal;
import java.util.Date;

public class OrderInvoiceResponse {
    private Long id;
    private Long orderId;
    private String invoiceNumber;
    private Date invoiceDate;
    private BigDecimal amount;
    private String pdf;

    public OrderInvoiceResponse(OrderInvoice invoice) {
        this.id = invoice.getId();
        this.orderId = invoice.getOrder().getId();
        this.invoiceNumber = invoice.getInvoiceNumber();
        this.invoiceDate = invoice.getInvoiceDate();
        this.amount = invoice.getAmount();
        this.pdf = invoice.getPdf();
    }

    // Getters
    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public Date getInvoiceDate() { return invoiceDate; }
    public BigDecimal getAmount() { return amount; }
    public String getPdf() { return pdf; }
}