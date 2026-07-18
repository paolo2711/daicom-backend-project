package com.daicom.daicombackend.clients;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;


public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByDeletedFalse();
    Optional<Client> findByIdAndDeletedFalse(Long id);
}