package com.daicom.daicombackend.company;

import com.daicom.daicombackend.company.dto.CompanyRequest;
import com.daicom.daicombackend.company.dto.CompanyResponse;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    private Company getMainCompany() {
        return companyRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Error: No existe compañía configurada."));
    }

    public CompanyResponse get() {
        return new CompanyResponse(getMainCompany());
    }

    public CompanyResponse update(CompanyRequest request) {
        Company company = getMainCompany();

        if (request.getName() != null)     company.setName(request.getName());
        if (request.getAddress() != null)   company.setAddress(request.getAddress());
        if (request.getPhone() != null)     company.setPhone(request.getPhone());
        if (request.getEmail() != null)     company.setEmail(request.getEmail());
        if (request.getDocument() != null)  company.setDocument(request.getDocument());

        if (request.getAccredited_correlative() != null)
            company.setAccreditedCorrelative(request.getAccredited_correlative());
        if (request.getNon_accredited_correlative() != null)
            company.setNonAccreditedCorrelative(request.getNon_accredited_correlative());
        if (request.getOperationality_correlative() != null)
            company.setOperationalCorrelative(request.getOperationality_correlative());
        if (request.getOrder_correlative() != null)
            company.setServiceOrderCorrelative(request.getOrder_correlative());

        return new CompanyResponse(companyRepository.save(company));
    }
}
