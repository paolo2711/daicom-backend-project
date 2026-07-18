package com.daicom.daicombackend.common.audit.dto;

import com.daicom.daicombackend.common.audit.LogEntry;
import java.time.LocalDateTime;

public class LogResponse {
    private Long id;
    private String username;
    private String action;
    private String entityAffected;
    private LocalDateTime createdAt;

    public LogResponse(LogEntry log) {
        this.id = log.getId();
        this.username = log.getUser().getUsername();
        this.action = log.getAction();
        this.entityAffected = log.getEntityAffected();
        this.createdAt = log.getCreatedAt();
    }

    // Getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getAction() { return action; }
    public String getEntityAffected() { return entityAffected; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}