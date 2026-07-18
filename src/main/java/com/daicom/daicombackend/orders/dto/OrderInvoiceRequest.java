package com.daicom.daicombackend.orders.dto;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.Date;

public class OrderInvoiceRequest {
    private String invoiceNumber;
    private Date invoiceDate;
    private BigDecimal amount;
    private MultipartFile file; // Para multipart/form-data

    // Getters y Setters...
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public Date getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(Date invoiceDate) { this.invoiceDate = invoiceDate; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public MultipartFile getFile() { return file; }
    public void setFile(MultipartFile file) { this.file = file; }
}