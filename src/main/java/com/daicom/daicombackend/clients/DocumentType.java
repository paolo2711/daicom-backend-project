package com.daicom.daicombackend.clients;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DocumentType {
    DNI(1, "DNI"),
    RUC(2, "RUC"),
    NO_DOCUMENT(3, "SIN DOCUMENTO");

    private final int code;
    private final String label;

    DocumentType(int code, String label) {
        this.code = code;
        this.label = label;
    }

    // el front usa 1/2/3
    @JsonValue
    public int getCode() { return code; }

    public String getLabel() { return label; }

    // convierte el numero al enum
    @JsonCreator
    public static DocumentType fromCode(int code) {
        for (DocumentType type : values()) {
            if (type.code == code) return type;
        }
        throw new IllegalArgumentException("Tipo de documento inválido: " + code);
    }
}
