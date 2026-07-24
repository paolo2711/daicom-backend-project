package com.daicom.daicombackend.roles;

import com.daicom.daicombackend.company.Company;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Rol de administrador: acceso irrestricto. No editable/eliminable.
    @Column(nullable = false)
    private boolean admin = false;

    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }
    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }
}
