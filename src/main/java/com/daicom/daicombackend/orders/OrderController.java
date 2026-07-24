package com.daicom.daicombackend.orders;

import com.daicom.daicombackend.orders.dto.OrderRequest;
import com.daicom.daicombackend.orders.dto.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    
    private final OrderService orderService;

    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @GetMapping
    public ResponseEntity<java.util.Map<String, Object>> getAll() {
        List<OrderResponse> orders = orderService.findAllOrders();
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("count", orders.size());
        response.put("results", orders);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/correlative")
    public ResponseEntity<java.util.Map<String, Object>> getNextCorrelative() {
        return ResponseEntity.ok(orderService.getNextOrderCorrelative());
    }

    // --- ENDPOINTS (Para que los Smart Chips de Vue no tiren error 404) ---
    @GetMapping("/summary/pending-payments")
    public ResponseEntity<java.util.Map<String, Integer>> getPendingPayments() {
        return ResponseEntity.ok(java.util.Collections.singletonMap("pending_payments", 0));
    }

    @GetMapping("/summary/pending-invoices")
    public ResponseEntity<java.util.Map<String, Integer>> getPendingInvoices() {
        return ResponseEntity.ok(java.util.Collections.singletonMap("pending_invoices", 0));
    }

    @GetMapping("/summary/afectas-detraccion")
    public ResponseEntity<java.util.Map<String, Integer>> getAfectasDetraccion() {
        return ResponseEntity.ok(java.util.Collections.singletonMap("afectas_detraccion", 0));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
        org.springframework.security.core.Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        
        return ResponseEntity.ok(orderService.createOrder(request, currentUsername));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable Long id, @Valid @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.updateOrder(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderResponse> patch(@PathVariable Long id, @Valid @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.syncOrderCertificates(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}