package com.qa.opencart.excelUtility;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelReader {
    
    public List<HashMap<String,Object>> readExcel(String filePath, String sheetName){
        Workbook workbook;
        FileInputStream fis;
        List<String> headers = new ArrayList<>();
        List<HashMap<String,Object>> listMap = new ArrayList<>();
        try {
            fis = new FileInputStream(new File(filePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            workbook = new XSSFWorkbook(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Sheet sheet = workbook.getSheet(sheetName);
        Row row = sheet.getRow(0);
        for(int i=0; i<row.getLastCellNum(); i++){
            headers.add(row.getCell(i).getStringCellValue());
        }
        System.out.println(headers);
        int rowNum = sheet.getPhysicalNumberOfRows();
        System.out.println(rowNum);
        for(int i=1; i<rowNum; i++){
            HashMap<String, Object> map = new HashMap<>();
            for(int j=0; j<headers.size(); j++){
                map.put(headers.get(j), getCellType(sheet.getRow(i).getCell(j)));
            }
            listMap.add(map);
        }
        return listMap;
        
    }
    
    public Cell getCellType(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                 cell.setCellValue(cell.getStringCellValue());
                 break;
            case NUMERIC:
                 cell.setCellValue(cell.getNumericCellValue());
                 break;
            case FORMULA:
                 cell.setCellValue(cell.getCellFormula());
            default:
                System.out.println("cell value provided is invalid");
        }
        return cell;
    }
}
