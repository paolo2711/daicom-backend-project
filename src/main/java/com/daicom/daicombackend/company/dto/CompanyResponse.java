package com.daicom.daicombackend.company.dto;

import com.daicom.daicombackend.company.Company;

/**
 * Datos de la empresa en snake_case, tal como los lee el frontend
 * (incluye los cuatro correlativos: acreditado, no acreditado, operatividad y orden).
 */
public class CompanyResponse {
    public Long id;
    public String name;
    public String address;
    public String phone;
    public String email;
    public String document;
    public Long accredited_correlative;
    public Long non_accredited_correlative;
    public Long operationality_correlative;
    public Long order_correlative;

    public CompanyResponse(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.address = company.getAddress();
        this.phone = company.getPhone();
        this.email = company.getEmail();
        this.document = company.getDocument();
        this.accredited_correlative = company.getAccreditedCorrelative();
        this.non_accredited_correlative = company.getNonAccreditedCorrelative();
        this.operationality_correlative = company.getOperationalCorrelative();
        this.order_correlative = company.getServiceOrderCorrelative();
    }
}
