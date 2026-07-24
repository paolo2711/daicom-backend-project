package com.daicom.daicombackend.certificates;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Prueba funcional del estampado de QR: genera un PDF base, le estampa el QR
 * y verifica que el resultado sea un PDF válido y de mayor tamaño (QR embebido).
 */
class PdfQrStamperTest {

    @Test
    void stampQr_generaPdfValidoConElQrEmbebido() throws Exception {
        PdfQrStamper stamper = new PdfQrStamper();

        // PDF base A4 en blanco
        File source = File.createTempFile("base", ".pdf");
        try (PDDocument doc = new PDDocument()) {
            doc.addPage(new PDPage(PDRectangle.A4));
            doc.save(source);
        }

        File output = File.createTempFile("con_qr", ".pdf");
        stamper.stampQr(source, output, "uuid-de-prueba-123");

        assertTrue(output.exists(), "El PDF de salida debe existir");
        assertTrue(output.length() > source.length(),
                "El PDF con QR debe pesar más que el base (imagen embebida)");

        // El resultado debe ser un PDF válido de 1 página
        try (PDDocument result = PDDocument.load(output)) {
            assertEquals(1, result.getNumberOfPages());
        }

        source.delete();
        output.delete();
    }
}
