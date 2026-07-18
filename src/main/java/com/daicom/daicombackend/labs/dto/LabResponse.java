package com.daicom.daicombackend.labs.dto;

import com.daicom.daicombackend.labs.Lab;

public class LabResponse {

    private Long id;
    private String name;
    private String code;

    public LabResponse(Lab lab) {
        this.id = lab.getId();
        this.name = lab.getName();
        this.code = lab.getCode();
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
}