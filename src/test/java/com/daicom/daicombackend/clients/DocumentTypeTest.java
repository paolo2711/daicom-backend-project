package com.daicom.daicombackend.clients;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias del enum DocumentType (mapeo código numérico <-> enum).
 * Cubre el bug corregido: el frontend envía 1/2/3 y deben mapear a DNI/RUC/SIN DOCUMENTO.
 */
class DocumentTypeTest {

    @Test
    void getCode_devuelveElCodigoNumericoCorrecto() {
        assertEquals(1, DocumentType.DNI.getCode());
        assertEquals(2, DocumentType.RUC.getCode());
        assertEquals(3, DocumentType.NO_DOCUMENT.getCode());
    }

    @Test
    void fromCode_mapeaCadaCodigoAlEnumCorrecto() {
        assertEquals(DocumentType.DNI, DocumentType.fromCode(1));
        assertEquals(DocumentType.RUC, DocumentType.fromCode(2));
        assertEquals(DocumentType.NO_DOCUMENT, DocumentType.fromCode(3));
    }

    @Test
    void getLabel_devuelveLaEtiquetaLegible() {
        assertEquals("DNI", DocumentType.DNI.getLabel());
        assertEquals("RUC", DocumentType.RUC.getLabel());
        assertEquals("SIN DOCUMENTO", DocumentType.NO_DOCUMENT.getLabel());
    }

    @Test
    void fromCode_conCodigoInvalido_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> DocumentType.fromCode(99));
    }
}
