package com.digicel.test.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Utilities {
	static WebDriver driver;
	public static final int IMPLICIT_WAIT_TIME=60; 
	public static final int PAGE_LOAD_TIME=10;
	public static String getTimeStamp() {
		Date date = new Date();
		return date.toString().replace(" ", "_").replace(":", "_");
	}
	
	 public static Object[][] fetchDataFromExcel(String sheetName) throws IOException {
	  
		 File excelFile = new File(System.getProperty("user.dir") + "\\src\\main\\java\\TestDataPackage\\LoginDetails.xlsx");

	        FileInputStream fis = new FileInputStream(excelFile);

	       
	        XSSFWorkbook workbook = new XSSFWorkbook(fis);
	        XSSFSheet sheet = workbook.getSheet(sheetName);

	        int rows = sheet.getPhysicalNumberOfRows();
	        int cols = sheet.getRow(0).getLastCellNum(); 

	        Object[][] data = new Object[rows - 1][cols]; 

	    
	        for (int i = 1; i < rows; i++) { 
	            XSSFRow row = sheet.getRow(i);

	            for (int j = 0; j < cols; j++) {
	                XSSFCell cell = row.getCell(j);

	                if (cell != null) {
	                    switch (cell.getCellType()) {
	                        case STRING:
	                            data[i - 1][j] = cell.getStringCellValue(); 
	                            break;
	                        case NUMERIC:
	                            if (DateUtil.isCellDateFormatted(cell)) {
	                                data[i - 1][j] = cell.getDateCellValue(); 
	                            } else {
	                                data[i - 1][j] = cell.getNumericCellValue();
	                            }
	                            break;
	                        case BOOLEAN:
	                            data[i - 1][j] = cell.getBooleanCellValue(); 
	                            break;
	                        default:
	                            data[i - 1][j] = null;
	                            break;
	                    }
	                } else {
	                    data[i - 1][j] = null; 
	                }
	            }
	        }

	        workbook.close();
	        fis.close();

	        return data;
	    }
	 public static void scroller(WebDriver driver) {
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript("window.scrollBy(0,500);");
	    }
	 public static void waitForElementToBeVisible(WebDriver driver, WebElement element) {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        wait.until(ExpectedConditions.visibilityOf(element));
	    }
}


