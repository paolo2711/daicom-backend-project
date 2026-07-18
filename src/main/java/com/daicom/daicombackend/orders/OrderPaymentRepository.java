package com.daicom.daicombackend.orders;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OrderPaymentRepository extends JpaRepository<OrderPayment, Long> {}