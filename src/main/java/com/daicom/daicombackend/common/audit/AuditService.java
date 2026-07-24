package com.daicom.daicombackend.common.audit;

import com.daicom.daicombackend.auth.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditService.class);

    private final LogEntryRepository logEntryRepository;

    public AuditService(LogEntryRepository logEntryRepository) {
        this.logEntryRepository = logEntryRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(User user, String action, String entityAffected) {
        LogEntry entry = new LogEntry();
        entry.setUser(user);
        entry.setAction(action);
        entry.setEntityAffected(entityAffected);
        logEntryRepository.save(entry);

        // queda tambien en el log de archivo
        log.info("AUDIT user='{}' action='{}' entity='{}'",
                user.getUsername(), action, entityAffected);
    }
}