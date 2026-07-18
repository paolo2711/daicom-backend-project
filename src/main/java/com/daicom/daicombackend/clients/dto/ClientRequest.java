package com.daicom.daicombackend.clients.dto;

import com.daicom.daicombackend.clients.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ClientRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotNull(message = "El tipo de documento es obligatorio")
    private DocumentType documentType;

    @NotBlank(message = "El documento es obligatorio")
    private String document;

    private String address;
    private String phone;
    private String email;

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public DocumentType getDocumentType() { return documentType; }
    public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }
    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}