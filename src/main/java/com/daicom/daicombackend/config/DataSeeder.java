package com.daicom.daicombackend.config;

import com.daicom.daicombackend.company.Company;
import com.daicom.daicombackend.company.CompanyRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CompanyRepository companyRepository;

    public DataSeeder(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (companyRepository.count() == 0) {
            Company company = new Company();
            company.setName("DAICOM S.A.C.");
            company.setAddress("Av. Principal 123, Arequipa");
            company.setPhone("054-123456");
            company.setEmail("contacto@daicom.com.pe");
            company.setAccreditedCorrelative(1L);
            company.setNonAccreditedCorrelative(1L);
            
            companyRepository.save(company);
            System.out.println("DataSeeder: Compañía inicial creada exitosamente.");
        }
    }
}