package com.daicom.daicombackend.orders;

import com.daicom.daicombackend.orders.dto.OrderPaymentRequest;
import com.daicom.daicombackend.orders.dto.OrderPaymentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderPaymentController {

    private final OrderService orderService;

    public OrderPaymentController(OrderService orderService) { this.orderService = orderService; }

    @PostMapping(value = "/{orderId}/payments", consumes = "multipart/form-data")
    public ResponseEntity<OrderPaymentResponse> addPayment(@PathVariable Long orderId, @ModelAttribute OrderPaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.addPayment(orderId, request));
    }

    @PutMapping(value = "/payments/{id}", consumes = "multipart/form-data")
    public ResponseEntity<OrderPaymentResponse> updatePayment(@PathVariable Long id, @ModelAttribute OrderPaymentRequest request) {
        return ResponseEntity.ok(orderService.updatePayment(id, request));
    }

    @DeleteMapping("/payments/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        orderService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}