package com.daicom.daicombackend.common.maintenance;

import com.daicom.daicombackend.common.audit.LogEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Component
public class MaintenanceTask {

    private static final Logger log = LoggerFactory.getLogger(MaintenanceTask.class);

    private final LogEntryRepository logEntryRepository;

    public MaintenanceTask(LogEntryRepository logEntryRepository) {
        this.logEntryRepository = logEntryRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanupOldLogs() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusMonths(6);
        logEntryRepository.deleteByCreatedAtBefore(cutoffDate);
        log.info("Mantenimiento: limpieza de logs de auditoría anteriores a {} ejecutada con éxito", cutoffDate);
    }
}