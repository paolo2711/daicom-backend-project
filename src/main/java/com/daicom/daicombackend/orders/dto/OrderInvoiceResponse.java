package com.daicom.daicombackend.orders.dto;

import com.daicom.daicombackend.orders.OrderInvoice;
import java.math.BigDecimal;

/**
 * Campos en snake_case para calzar con lo que el frontend lee
 * (invoice_number, invoice_date, amount, pdf_url).
 */
public class OrderInvoiceResponse {
    public Long id;
    public Long order_id;
    public String invoice_number;
    public String invoice_date;
    public BigDecimal amount;
    public String pdf_url;

    public OrderInvoiceResponse(OrderInvoice invoice) {
        this.id = invoice.getId();
        this.order_id = invoice.getOrder().getId();
        this.invoice_number = invoice.getInvoiceNumber();
        this.invoice_date = invoice.getInvoiceDate() != null
                ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(invoice.getInvoiceDate())
                : null;
        this.amount = invoice.getAmount();
        this.pdf_url = invoice.getPdf();
    }
}
