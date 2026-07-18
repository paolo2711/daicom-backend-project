package com.daicom.daicombackend.common.audit;

import com.daicom.daicombackend.auth.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

    private final LogEntryRepository logEntryRepository;

    public AuditService(LogEntryRepository logEntryRepository) {
        this.logEntryRepository = logEntryRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(User user, String action, String entityAffected) {
        LogEntry log = new LogEntry();
        log.setUser(user);
        log.setAction(action);
        log.setEntityAffected(entityAffected);
        logEntryRepository.save(log);
    }
}