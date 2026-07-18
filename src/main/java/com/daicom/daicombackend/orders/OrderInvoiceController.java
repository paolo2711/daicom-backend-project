package com.daicom.daicombackend.orders;

import com.daicom.daicombackend.orders.dto.OrderInvoiceRequest;
import com.daicom.daicombackend.orders.dto.OrderInvoiceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderInvoiceController {

    private final OrderService orderService;

    public OrderInvoiceController(OrderService orderService) { this.orderService = orderService; }

    @GetMapping("/{orderId}/invoices")
    public ResponseEntity<List<OrderInvoiceResponse>> getInvoicesByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getInvoicesByOrderId(orderId));
    }

    @PostMapping(value = "/{orderId}/invoices", consumes = "multipart/form-data")
    public ResponseEntity<OrderInvoiceResponse> addInvoice(@PathVariable Long orderId, @ModelAttribute OrderInvoiceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addInvoice(orderId, request));
    }

    @PutMapping(value = "/invoices/{id}", consumes = "multipart/form-data")
    public ResponseEntity<OrderInvoiceResponse> updateInvoice(@PathVariable Long id, @ModelAttribute OrderInvoiceRequest request) {
        return ResponseEntity.ok(orderService.updateInvoice(id, request));
    }

    @DeleteMapping("/invoices/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        orderService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }
}