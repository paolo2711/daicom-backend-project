package com.daicom.daicombackend.common.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
    void deleteByCreatedAtBefore(LocalDateTime cutoffDate);
}