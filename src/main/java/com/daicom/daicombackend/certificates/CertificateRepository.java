package com.daicom.daicombackend.certificates;

import org.springframework.data.jpa.repository.JpaRepository;



public interface CertificateRepository extends JpaRepository<Certificate, Long> {
}