package com.digicel.test.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReporter {


public static ExtentReports getExtentReport()
{
	ExtentReports extentreport =new ExtentReports();
	File extentReportFile=new File("C:\\Users\\FINCART\\eclipse-workspace\\digicel\\test-output\\ExtentReportsFolder\\extentreport.html");
	ExtentSparkReporter sparkReporter=new 	ExtentSparkReporter(extentReportFile);
	sparkReporter.config().setTheme(Theme.DARK);
	sparkReporter.config().setDocumentTitle("digicel automation test result");
	sparkReporter.config().setReportName("digicel  report");
	extentreport.attachReporter(sparkReporter);
	Properties ListernerProp=new Properties();
	ListernerProp=new Properties();
	File ListernerPropFile=new File("src\\main\\java\\propertiesfile\\propertyfile.properties");
	
	try {
		FileInputStream fis = new FileInputStream(ListernerPropFile);
		ListernerProp.load(fis);
		
	} catch ( Throwable e) {
		
		e.printStackTrace();
	}
	extentreport.setSystemInfo("Application url", ListernerProp.getProperty("url"));
	extentreport.setSystemInfo("email", ListernerProp.getProperty("validEmail"));
	extentreport.setSystemInfo("browser Name", ListernerProp.getProperty("browserName"));
	extentreport.setSystemInfo("valid Password", ListernerProp.getProperty("validPassword"));
	return extentreport;
	
	
}
  
}
