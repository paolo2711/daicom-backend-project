package com.daicom.daicombackend.company.dto;

/**
 * El front manda multipart en snake_case. Los archivos no se usan por ahora.
 */
public class CompanyRequest {
    private String name;
    private String address;
    private String phone;
    private String email;
    private String document;
    private Long accredited_correlative;
    private Long non_accredited_correlative;
    private Long operationality_correlative;
    private Long order_correlative;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDocument() { return document; }
    public void setDocument(String document) { this.document = document; }
    public Long getAccredited_correlative() { return accredited_correlative; }
    public void setAccredited_correlative(Long v) { this.accredited_correlative = v; }
    public Long getNon_accredited_correlative() { return non_accredited_correlative; }
    public void setNon_accredited_correlative(Long v) { this.non_accredited_correlative = v; }
    public Long getOperationality_correlative() { return operationality_correlative; }
    public void setOperationality_correlative(Long v) { this.operationality_correlative = v; }
    public Long getOrder_correlative() { return order_correlative; }
    public void setOrder_correlative(Long v) { this.order_correlative = v; }
}
