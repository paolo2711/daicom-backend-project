package com.daicom.daicombackend.reports;

import com.daicom.daicombackend.orders.Order;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

// arma el reporte de cobranzas (.xlsx) con Apache POI
@Component
public class OrderExcelExporter {

    private static final String[] COLUMNAS = {
            "N° Orden", "Cliente", "Fecha", "Facturado", "Abonado", "Saldo", "Estado"
    };

    public byte[] build(List<Order> orders) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Cuentas por cobrar");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle moneyStyle = workbook.createCellStyle();
            moneyStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

            CellStyle totalStyle = workbook.createCellStyle();
            Font totalFont = workbook.createFont();
            totalFont.setBold(true);
            totalStyle.setFont(totalFont);
            totalStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

            CellStyle totalLabelStyle = workbook.createCellStyle();
            totalLabelStyle.setFont(totalFont);

            // Encabezados
            Row header = sheet.createRow(0);
            for (int i = 0; i < COLUMNAS.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(COLUMNAS[i]);
                cell.setCellStyle(headerStyle);
            }

            DateTimeFormatter fecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            BigDecimal totFacturado = BigDecimal.ZERO;
            BigDecimal totAbonado = BigDecimal.ZERO;
            BigDecimal totSaldo = BigDecimal.ZERO;

            int fila = 1;
            for (Order o : orders) {
                BigDecimal facturado = o.getTotalFacturado();
                BigDecimal abonado = o.getTotalPagado();
                BigDecimal saldo = o.getSaldoPendiente();

                totFacturado = totFacturado.add(facturado);
                totAbonado = totAbonado.add(abonado);
                totSaldo = totSaldo.add(saldo);

                Row row = sheet.createRow(fila++);
                row.createCell(0).setCellValue(o.getOrderNumber());
                row.createCell(1).setCellValue(o.getClient() != null ? o.getClient().getName() : "");
                row.createCell(2).setCellValue(o.getCreatedAt() != null ? o.getCreatedAt().format(fecha) : "");

                Cell cFact = row.createCell(3);
                cFact.setCellValue(facturado.doubleValue());
                cFact.setCellStyle(moneyStyle);

                Cell cAbon = row.createCell(4);
                cAbon.setCellValue(abonado.doubleValue());
                cAbon.setCellStyle(moneyStyle);

                Cell cSaldo = row.createCell(5);
                cSaldo.setCellValue(saldo.doubleValue());
                cSaldo.setCellStyle(moneyStyle);

                row.createCell(6).setCellValue(estadoLabel(o.getStatus()));
            }

            // Fila de totales
            Row totalRow = sheet.createRow(fila);
            Cell etiqueta = totalRow.createCell(2);
            etiqueta.setCellValue("TOTALES");
            etiqueta.setCellStyle(totalLabelStyle);

            Cell tFact = totalRow.createCell(3);
            tFact.setCellValue(totFacturado.doubleValue());
            tFact.setCellStyle(totalStyle);

            Cell tAbon = totalRow.createCell(4);
            tAbon.setCellValue(totAbonado.doubleValue());
            tAbon.setCellStyle(totalStyle);

            Cell tSaldo = totalRow.createCell(5);
            tSaldo.setCellValue(totSaldo.doubleValue());
            tSaldo.setCellStyle(totalStyle);

            for (int i = 0; i < COLUMNAS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo generar el Excel: " + e.getMessage(), e);
        }
    }

    private String estadoLabel(Integer status) {
        if (status == null) return "";
        switch (status) {
            case 1: return "En Proceso";
            case 2: return "Deuda";
            case 3: return "Abonado";
            case 5: return "Pagado";
            default: return "";
        }
    }
}
