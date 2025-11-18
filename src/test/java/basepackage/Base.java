package basepackage;

import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.digicel.test.utils.ScreenshotUtil;
import com.digicel.test.utils.Utilities;

public class Base {
    public static WebDriver driver;
    protected WebDriverWait wait;
    public Properties prop;

    public Base() {
        prop = new Properties();
        try (InputStream fis = getClass().getClassLoader()
                .getResourceAsStream("propertyfile.properties")) {
            if (fis != null) {
                prop.load(fis);
            } else {
                throw new RuntimeException("propertyfile.properties not found in classpath");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeSuite
    public void setUpSuite() {
        driver = initializeBrowserAndUrl(prop.getProperty("browserName"));
        wait = new WebDriverWait(driver, Duration.ofSeconds(Utilities.IMPLICIT_WAIT_TIME));
    }

    @AfterSuite
    public void tearDownSuite() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterMethod
    public void logResultAndScreenshot(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            ScreenshotUtil.captureScreenshot(driver, result.getName());
        }
    }

    public WebDriver initializeBrowserAndUrl(String browserName) {
        try {
            switch (browserName.toLowerCase()) {
                case "chrome":
                    driver = new ChromeDriver();
                    break;
                case "firefox":
                    driver = new FirefoxDriver();
                    break;
                case "safari":
                    driver = new SafariDriver();
                    break;
                case "edge":
                    driver = new EdgeDriver();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browserName);
            }

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
            driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));

            // Retry logic for page load
            int maxRetries = 3;
            int retryCount = 0;
            boolean loaded = false;
            while (!loaded && retryCount < maxRetries) {
                try {
                    driver.get(prop.getProperty("url"));
                    loaded = true;
                } catch (Exception e) {
                    retryCount++;
                    if (retryCount >= maxRetries) throw e;
                    Thread.sleep(2000);
                }
            }

            return driver;
        } catch (Exception e) {
            if (driver != null) driver.quit();
            throw new RuntimeException("Failed to initialize browser: " + e.getMessage(), e);
        }
    }
}
