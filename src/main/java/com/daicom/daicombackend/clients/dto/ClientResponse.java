package com.daicom.daicombackend.clients.dto;

import com.daicom.daicombackend.clients.Client;

public class ClientResponse {

    private Long id;
    private String name;
    private String documentType;
    private String document;
    private String address;
    private String phone;
    private String email;

    public ClientResponse(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.documentType = client.getDocumentType().name();
        this.document = client.getDocument();
        this.address = client.getAddress();
        this.phone = client.getPhone();
        this.email = client.getEmail();
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDocumentType() { return documentType; }
    public String getDocument() { return document; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
}