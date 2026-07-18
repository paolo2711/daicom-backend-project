package com.daicom.daicombackend.company;

import jakarta.persistence.*;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;
    private String phone;
    private String email;

    @Column(nullable = false)
    private Long accreditedCorrelative = 1L;

    @Column(nullable = false)
    private Long nonAccreditedCorrelative = 1L;

    @Column(nullable = false)
    private Long operationalCorrelative = 1L;

    @Column(nullable = false)
    private Long serviceOrderCorrelative = 1L;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Long getAccreditedCorrelative() { return accreditedCorrelative; }
    public void setAccreditedCorrelative(Long accreditedCorrelative) { this.accreditedCorrelative = accreditedCorrelative; }
    
    public Long getNonAccreditedCorrelative() { return nonAccreditedCorrelative; }
    public void setNonAccreditedCorrelative(Long nonAccreditedCorrelative) { this.nonAccreditedCorrelative = nonAccreditedCorrelative; }

    public Long getOperationalCorrelative() { return operationalCorrelative; }
    public void setOperationalCorrelative(Long operationalCorrelative) { this.operationalCorrelative = operationalCorrelative; }

    public Long getServiceOrderCorrelative() { return serviceOrderCorrelative; }
    public void setServiceOrderCorrelative(Long serviceOrderCorrelative) { this.serviceOrderCorrelative = serviceOrderCorrelative; }
}