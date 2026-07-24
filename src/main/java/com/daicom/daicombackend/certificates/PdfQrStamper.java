package com.daicom.daicombackend.certificates;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Genera un código QR (con el contenido dado) y lo estampa sobre un PDF existente.
 * Posición: la primera página se divide en una grilla de 5 filas × 3 columnas;
 * el QR se coloca en la 4ta fila (desde arriba), centrado en el borde entre la
 * 2da y la 3ra columna.
 */
@Component
public class PdfQrStamper {

    /**
     * @param sourcePdf  PDF base sobre el que estampar.
     * @param outputPdf  Destino del PDF resultante (con el QR).
     * @param qrContent  Texto a codificar en el QR (aquí, el UUID del certificado).
     */
    public void stampQr(File sourcePdf, File outputPdf, String qrContent) throws Exception {
        try (PDDocument document = PDDocument.load(sourcePdf)) {
            PDPage page = document.getPage(0);
            PDRectangle box = page.getMediaBox();
            float width = box.getWidth();
            float height = box.getHeight();

            float colWidth = width / 3f;
            float rowHeight = height / 5f;

            // tamaño del QR segun la celda
            float qrSize = Math.min(colWidth, rowHeight) * 0.8f;

            // Centro X = borde entre columna 2 y 3 (2 * colWidth desde la izquierda)
            float centerX = 2f * colWidth;
            // Centro Y (origen abajo-izquierda) de la 4ta fila desde arriba
            float centerY = height - (3.5f * rowHeight);

            float x = centerX - qrSize / 2f;
            float y = centerY - qrSize / 2f;

            BufferedImage qrImage = generateQrImage(qrContent, 300);
            PDImageXObject qrObject = LosslessFactory.createFromImage(document, qrImage);

            try (PDPageContentStream cs = new PDPageContentStream(
                    document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                cs.drawImage(qrObject, x, y, qrSize, qrSize);
            }

            document.save(outputPdf);
        }
    }

    private BufferedImage generateQrImage(String content, int sizePx) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, sizePx, sizePx);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }
}
