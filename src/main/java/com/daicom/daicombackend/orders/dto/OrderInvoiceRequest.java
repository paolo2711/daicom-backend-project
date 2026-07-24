package com.daicom.daicombackend.orders.dto;
import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.Date;

public class OrderInvoiceRequest {
    private String invoiceNumber;
    private Date invoiceDate;
    private BigDecimal amount;
    private MultipartFile file; // Para multipart/form-data

    // Getters (usados por el service)
    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }
    public Date getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(Date invoiceDate) { this.invoiceDate = invoiceDate; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public MultipartFile getFile() { return file; }
    public void setFile(MultipartFile file) { this.file = file; }

    // --- Adaptadores para capturar FormData (snake_case) del Frontend ---
    public void setInvoice_number(String invoice_number) { this.invoiceNumber = invoice_number; }
    public void setInvoice_date(String invoice_date) {
        try {
            this.invoiceDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(invoice_date);
        } catch (java.text.ParseException e) {
            this.invoiceDate = null;
        }
    }
    // El frontend envía el archivo con el nombre de campo 'pdf'
    public void setPdf(MultipartFile pdf) { this.file = pdf; }
}
