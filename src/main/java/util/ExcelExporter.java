package util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.table.TableModel;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ExcelExporter {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static void export(TableModel model, String filePath) throws Exception {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Report Data");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < model.getColumnCount(); col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(model.getColumnName(col));
                cell.setCellStyle(headerStyle);
            }

            for (int row = 0; row < model.getRowCount(); row++) {
                Row dataRow = sheet.createRow(row + 1);
                for (int col = 0; col < model.getColumnCount(); col++) {
                    Cell cell = dataRow.createCell(col);
                    Object value = model.getValueAt(row, col);
                    setCellValue(cell, value, dateStyle);
                }
            }

            for (int i = 0; i < model.getColumnCount(); i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }
    }

    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private static CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(
                createHelper.createDataFormat().getFormat("yyyy-mm-dd"));
        return style;
    }

    private static void setCellValue(Cell cell, Object value, CellStyle dateStyle) {
        if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof LocalDate) {
            cell.setCellValue(((LocalDate) value).format(DATE_FORMATTER));
            cell.setCellStyle(dateStyle);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
    }
}