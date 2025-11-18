package com.digicel.listerners;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class Listerners implements ITestListener {

    ExtentReports extentreport;
    ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        extentreport = com.digicel.test.utils.ExtentReporter.getExtentReport();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getName();
        ExtentTest test = extentreport.createTest(testName);
        extentTest.set(test);
        test.log(Status.INFO, testName + " Test started");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, result.getName() + " Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getName();
        WebDriver driver = null;

        try {
            Object testInstance = result.getInstance();
            driver = (WebDriver) testInstance.getClass().getDeclaredField("driver").get(testInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (driver != null) {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String destPath = System.getProperty("user.dir") + "\\ScreenShots\\" + testName + ".png";

            File folder = new File(System.getProperty("user.dir") + "\\ScreenShots");
            if (!folder.exists()) folder.mkdirs();

            try {
                FileHandler.copy(screenshot, new File(destPath));
                extentTest.get().addScreenCaptureFromPath(destPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        extentTest.get().log(Status.FAIL, testName + " Test failed");
        extentTest.get().log(Status.INFO, result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP, result.getName() + " Test skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        if (extentreport != null) {
            extentreport.flush();
        }
    }
}
