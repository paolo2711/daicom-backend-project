package com.daicom.daicombackend.labs.dto;

import jakarta.validation.constraints.NotBlank;

public class LabRequest {

    @NotBlank(message = "El nombre del laboratorio es obligatorio")
    private String name;

    @NotBlank(message = "El código es obligatorio")
    private String code;

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}