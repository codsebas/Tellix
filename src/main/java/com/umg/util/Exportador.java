package com.umg.util;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.table.TableModel;
import java.io.*;

public class Exportador {

    // --- Exportar a Excel ---
    public static void exportarExcel(TableModel model, String titulo, File destino) throws IOException {
        Workbook wb = new XSSFWorkbook();
        Sheet hoja = wb.createSheet("Reporte");
        int filaIndex = 0;

        // Estilo de título
        CellStyle estiloTitulo = wb.createCellStyle();
        Font fuenteTitulo = wb.createFont();
        fuenteTitulo.setBold(true);
        fuenteTitulo.setFontHeightInPoints((short)14);
        estiloTitulo.setFont(fuenteTitulo);
        estiloTitulo.setAlignment(HorizontalAlignment.CENTER);

        // Título
        Row filaTitulo = hoja.createRow(filaIndex++);
        Cell celdaTitulo = filaTitulo.createCell(0);
        celdaTitulo.setCellValue(titulo);
        celdaTitulo.setCellStyle(estiloTitulo);
        hoja.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, model.getColumnCount()-1));

        filaIndex++; // fila vacía

        // Encabezados
        Row encabezado = hoja.createRow(filaIndex++);
        CellStyle estiloEncabezado = wb.createCellStyle();
        Font fuenteEncabezado = wb.createFont();
        fuenteEncabezado.setBold(true);
        estiloEncabezado.setFont(fuenteEncabezado);

        for (int c = 0; c < model.getColumnCount(); c++) {
            Cell celda = encabezado.createCell(c);
            celda.setCellValue(model.getColumnName(c));
            celda.setCellStyle(estiloEncabezado);
        }

        // Datos
        for (int r = 0; r < model.getRowCount(); r++) {
            Row fila = hoja.createRow(filaIndex++);
            for (int c = 0; c < model.getColumnCount(); c++) {
                Cell celda = fila.createCell(c);
                Object valor = model.getValueAt(r, c);
                celda.setCellValue(valor == null ? "" : valor.toString());
            }
        }

        for (int c = 0; c < model.getColumnCount(); c++)
            hoja.autoSizeColumn(c);

        try (FileOutputStream fos = new FileOutputStream(destino)) {
            wb.write(fos);
        }
        wb.close();
    }

    // --- Exportar a PDF ---
    public static void exportarPDF(TableModel model, String titulo, File destino) throws Exception {
        Document doc = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(doc, new FileOutputStream(destino));
        doc.open();

        com.itextpdf.text.Font fontTitulo =
                com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD, 16);

        Paragraph pTitulo = new Paragraph(titulo, fontTitulo);
        pTitulo.setAlignment(Element.ALIGN_CENTER);
        pTitulo.setSpacingAfter(15);
        doc.add(pTitulo);

        PdfPTable tabla = new PdfPTable(model.getColumnCount());
        tabla.setWidthPercentage(100);

        // Encabezados
        for (int c = 0; c < model.getColumnCount(); c++) {
            PdfPCell cell = new PdfPCell(new Phrase(model.getColumnName(c)));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tabla.addCell(cell);
        }

        // Datos
        for (int r = 0; r < model.getRowCount(); r++) {
            for (int c = 0; c < model.getColumnCount(); c++) {
                Object valor = model.getValueAt(r, c);
                tabla.addCell(valor == null ? "" : valor.toString());
            }
        }

        doc.add(tabla);
        doc.close();
    }
}