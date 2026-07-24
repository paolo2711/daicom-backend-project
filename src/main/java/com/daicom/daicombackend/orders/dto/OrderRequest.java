package com.daicom.daicombackend.orders.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class OrderRequest {
    
    @NotNull(message = "El cliente es obligatorio")
    private Long clientId;

    private Integer orderType = 1;

    private boolean wantsInvoice = true;

    // Esta variable atrapará la lista de equipos que envía el frontend
    private List<Map<String, Object>> items;

    // IDs de certificados (separados por coma) a los que se debe sincronizar el nuevo cliente
    private String syncCertificates;

    // Getters y Setters originales
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public Integer getOrderType() { return orderType; }
    public void setOrderType(Integer orderType) { this.orderType = orderType; }

    public boolean isWantsInvoice() { return wantsInvoice; }
    public void setWantsInvoice(boolean wantsInvoice) { this.wantsInvoice = wantsInvoice; }

    public List<Map<String, Object>> getItems() { return items; }
    public void setItems(List<Map<String, Object>> items) { this.items = items; }

    public String getSyncCertificates() { return syncCertificates; }
    public void setSyncCertificates(String syncCertificates) { this.syncCertificates = syncCertificates; }

    // --- ADAPTADORES MÁGICOS PARA VUE ---
    // Cuando Vue mande "client", Java lo guardará en "clientId"
    public void setClient(Long client) { this.clientId = client; }
    
    // Cuando Vue mande "order_type", Java lo guardará en "orderType"
    public void setOrder_type(Integer order_type) { this.orderType = order_type; }

    // Cuando Vue mande "sync_certificates", Java lo guardará en "syncCertificates"
    public void setSync_certificates(String sync_certificates) { this.syncCertificates = sync_certificates; }
}