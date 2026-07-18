package com.daicom.daicombackend.orders.dto;

import com.daicom.daicombackend.orders.PaymentMethod;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

public class OrderPaymentRequest {
    
    @NotNull(message = "El monto es obligatorio")
    private BigDecimal amount;
    
    @NotNull(message = "El método de pago es obligatorio")
    private PaymentMethod paymentMethod;
    
    @NotNull(message = "La fecha de pago es obligatoria")
    private Date paymentDate;
    
    private String notes;
    private MultipartFile file;
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public MultipartFile getFile() { return file; }
    public void setFile(MultipartFile file) { this.file = file; }
}