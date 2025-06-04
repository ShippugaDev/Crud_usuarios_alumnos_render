package com.tecsup.demo.views;

import com.tecsup.demo.domain.entities.Alumno;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Component("alumno/ver.xlsx")
public class AlumnoXlsView extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\"alumnos.xlsx\"");

        List<Alumno> alumnos = (List<Alumno>) model.get("alumnos");
        Sheet sheet = workbook.createSheet("Alumnos");

        // Estilo para la cabecera
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);

        // Estilo para el contenido de la tabla
        CellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setBorderBottom(BorderStyle.THIN);
        bodyStyle.setBorderTop(BorderStyle.THIN);
        bodyStyle.setBorderRight(BorderStyle.THIN);
        bodyStyle.setBorderLeft(BorderStyle.THIN);

        // Crear cabecera
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Nombre");
        headerRow.createCell(2).setCellValue("Apellido");
        headerRow.createCell(3).setCellValue("Email");
        headerRow.createCell(4).setCellValue("Teléfono");

        // Aplicar estilo de cabecera
        for (int i = 0; i < 5; i++) {
            headerRow.getCell(i).setCellStyle(headerStyle);
        }

        // Rellenar la tabla con los datos de los alumnos
        int rowNum = 1;
        for (Alumno alumno : alumnos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(alumno.getId());
            row.createCell(1).setCellValue(alumno.getNombre());
            row.createCell(2).setCellValue(alumno.getApellido());
            row.createCell(3).setCellValue(alumno.getEmail());
            row.createCell(4).setCellValue(alumno.getTelefono());

            // Aplicar estilo de cuerpo
            for (int i = 0; i < 5; i++) {
                row.getCell(i).setCellStyle(bodyStyle);
            }
        }
    }
}
