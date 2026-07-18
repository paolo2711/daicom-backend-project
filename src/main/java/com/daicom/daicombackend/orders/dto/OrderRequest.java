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

    // Getters y Setters originales
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    
    public Integer getOrderType() { return orderType; }
    public void setOrderType(Integer orderType) { this.orderType = orderType; }

    public boolean isWantsInvoice() { return wantsInvoice; }
    public void setWantsInvoice(boolean wantsInvoice) { this.wantsInvoice = wantsInvoice; }

    public List<Map<String, Object>> getItems() { return items; }
    public void setItems(List<Map<String, Object>> items) { this.items = items; }

    // --- ADAPTADORES MÁGICOS PARA VUE ---
    // Cuando Vue mande "client", Java lo guardará en "clientId"
    public void setClient(Long client) { this.clientId = client; }
    
    // Cuando Vue mande "order_type", Java lo guardará en "orderType"
    public void setOrder_type(Integer order_type) { this.orderType = order_type; }
}