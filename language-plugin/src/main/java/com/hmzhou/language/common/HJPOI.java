package com.hmzhou.language.common;

import com.hmzhou.language.common.utils.CloseUtils;
import com.hmzhou.language.common.utils.Log;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * POI 操作类
 */
public class HJPOI {

    private String mFilePath;
    private Workbook mWorkbook;

    public HJPOI(String filePath) {
        this.mFilePath = filePath;
        this.mWorkbook = openWorkbook(mFilePath);
        if (mWorkbook == null) {
            throw new IllegalArgumentException("Excel 文件路径或文件格式错误");
        }
    }

    public void close() {
        CloseUtils.closeIO(mWorkbook);
    }

    private Workbook openWorkbook(String filePath) {

        InputStream inputStream = null;
        Workbook workbook = null;

        try {
            inputStream = new FileInputStream(new File(filePath));
            workbook = WorkbookFactory.create(inputStream);
            inputStream.close();
        } catch (Exception e) {
            // do nothing
        } finally {
            CloseUtils.closeIO(inputStream);
        }
        return workbook;
    }


    public List<String> readRow(int sheetNum, int rowNum, int start, int end) {
        int totalSheet = mWorkbook.getNumberOfSheets();
        if (sheetNum > totalSheet || (start > end)) {
            Log.debug("当前sheet 总数( " + totalSheet + " ) 少于 " + sheetNum + " ");
            return Collections.emptyList();
        }
        Sheet sheet = mWorkbook.getSheetAt(sheetNum);

        int startRow = sheet.getFirstRowNum();
        int endRow = sheet.getLastRowNum();

        if (rowNum < startRow || rowNum >= endRow) {
            return Collections.emptyList();
        }

        Row row = sheet.getRow(rowNum);
        if (row == null) {
            return Collections.emptyList();
        }

        int cellCount = row.getPhysicalNumberOfCells();
        List<String> datas = new ArrayList<>(end - start + 1);
        int count = end;
        if (cellCount < end) {
            count = cellCount;
        }
        int max = Math.max(count, cellCount);

        // 此处没做start的校验
        for (int i = start; i < count; i++) {
            datas.add(readCell(row.getCell(i)));
        }
        for (int i = count; i < max; i++) {
            datas.add(null);
        }

        return datas;
    }


    public List<String> readColumn(int sheetNum, int columnNum, int start, int end) {
        int totalSheet = mWorkbook.getNumberOfSheets();
        if (sheetNum > totalSheet || (start > end)) {
            Log.debug("当前sheet 总数( " + totalSheet + " ) 少于 " + sheetNum + " ");
            return Collections.emptyList();
        }
        Sheet sheet = mWorkbook.getSheetAt(sheetNum);

        List<String> datas = new ArrayList<>(end - start + 1);
        for (int i = start; i < end; i++) {
            Row row = sheet.getRow(i);
            Cell cell = null;
            try {
                cell = row.getCell(columnNum);
            } catch (Exception e) {
                // do nothing
            }

            datas.add(readCell(cell));
        }

        return datas;
    }

    public int getMaxRowCount(int sheetNum) {
        Sheet sheet = mWorkbook.getSheetAt(sheetNum);
        return sheet.getLastRowNum() + 1;
    }

    public int getMaxColumnCount(int sheetNum, int rowNum) {
        Sheet sheet = mWorkbook.getSheetAt(sheetNum);
        Row row = sheet.getRow(rowNum);
        if (row == null) return 0;
        return row.getPhysicalNumberOfCells();
    }

    private String readCell(Cell cell) {
        if (cell == null) {
            return null;
        }
        String value;
        if (cell.getCellTypeEnum() == CellType.NUMERIC) {
            value = cell.getNumericCellValue() + "";
        } else if (cell.getCellTypeEnum() == CellType.STRING) {
            value = cell.getStringCellValue();
        } else if (cell.getCellTypeEnum() == CellType.ERROR) {
            value = cell.getErrorCellValue() + "";
        } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
            value = cell.getBooleanCellValue() + "";
        } else {
            value = null;
        }
        return value;
    }
}
