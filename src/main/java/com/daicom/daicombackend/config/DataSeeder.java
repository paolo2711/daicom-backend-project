package com.daicom.daicombackend.config;

import com.daicom.daicombackend.auth.Role;
import com.daicom.daicombackend.auth.User;
import com.daicom.daicombackend.auth.UserRepository;
import com.daicom.daicombackend.company.Company;
import com.daicom.daicombackend.company.CompanyRepository;
import com.daicom.daicombackend.roles.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CompanyRepository companyRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public DataSeeder(CompanyRepository companyRepository, RoleRepository roleRepository,
                      UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Company company;
        if (companyRepository.count() == 0) {
            company = new Company();
            company.setName("DAICOM S.A.C.");
            company.setAddress("Av. Principal 123, Arequipa");
            company.setPhone("054-123456");
            company.setEmail("contacto@daicom.com.pe");
            company.setAccreditedCorrelative(1L);
            company.setNonAccreditedCorrelative(1L);

            companyRepository.save(company);
            System.out.println("DataSeeder: Compañía inicial creada exitosamente.");
        } else {
            company = companyRepository.findAll().get(0);
        }

        // Rol de administrador (acceso irrestricto). Se crea una sola vez.
        com.daicom.daicombackend.roles.Role adminRole = roleRepository.findFirstByAdminTrue().orElse(null);
        if (adminRole == null) {
            adminRole = new com.daicom.daicombackend.roles.Role();
            adminRole.setName("Administrador");
            adminRole.setAdmin(true);
            adminRole.setCompany(company);
            adminRole = roleRepository.save(adminRole);
            System.out.println("DataSeeder: Rol 'Administrador' creado.");
        }

        // Enlaza los usuarios ADMIN existentes (enum) al rol granular Administrador
        final com.daicom.daicombackend.roles.Role finalAdminRole = adminRole;
        userRepository.findAll().forEach(user -> {
            if (user.getRoleEntity() == null && user.getRole() == Role.ADMIN) {
                user.setRoleEntity(finalAdminRole);
                userRepository.save(user);
            }
        });
    }
}
