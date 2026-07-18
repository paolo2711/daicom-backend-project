package com.daicom.daicombackend.auth.dto;

public class ProfileResponse {
    
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String role;

    public ProfileResponse(Long id, String username, String firstName, String lastName, String email, String role) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    // Getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}