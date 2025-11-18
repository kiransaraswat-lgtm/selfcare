package com.digicel.test.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtil {

    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);

        String destDir = "test-output/screenshots"; // Folder where screenshots will go
        String destPath = destDir + "/" + screenshotName + "_" + System.currentTimeMillis() + ".png";

        try {
            Files.createDirectories(Paths.get(destDir)); // Make sure directory exists
            File destination = new File(destPath);
            Files.copy(source.toPath(), destination.toPath());
            return destination.getAbsolutePath(); // Return full path for Extent Report
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}