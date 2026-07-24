package com.daicom.daicombackend.common.audit.dto;

import com.daicom.daicombackend.common.audit.LogEntry;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class LogResponse {
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    private Long id;
    private String username;
    private String action;
    private String entityAffected;
    private String description;
    private String log_date;
    private String log_time;
    private Map<String, Object> user_data;

    public LogResponse(LogEntry log) {
        this.id = log.getId();
        this.username = log.getUser().getUsername();
        this.action = log.getAction();
        this.entityAffected = log.getEntityAffected();
        this.description = friendlyAction(log.getAction()) + " (" + log.getEntityAffected() + ")";

        LocalDateTime createdAt = log.getCreatedAt();
        this.log_date = createdAt != null ? createdAt.format(DATE_FMT) : null;
        this.log_time = createdAt != null ? createdAt.format(TIME_FMT) : null;

        this.user_data = new HashMap<>();
        this.user_data.put("id", log.getUser().getId());
        this.user_data.put("username", log.getUser().getUsername());
    }

    private static String friendlyAction(String action) {
        switch (action) {
            case "CREATE_ORDER": return "Creó una nueva orden";
            case "CREATE_CERTIFICATE": return "Creó un nuevo certificado";
            case "CREATE_CLIENT": return "Registró un nuevo cliente";
            case "CREATE_LAB": return "Registró un nuevo laboratorio";
            default: return action;
        }
    }

    // Getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getAction() { return action; }
    public String getEntityAffected() { return entityAffected; }
    public String getDescription() { return description; }
    public String getLog_date() { return log_date; }
    public String getLog_time() { return log_time; }
    public Map<String, Object> getUser_data() { return user_data; }
}
