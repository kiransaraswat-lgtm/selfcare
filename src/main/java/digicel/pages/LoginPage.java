package digicel.pages;

import java.time.Duration;
import java.util.List;
import java.util.Iterator;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    @FindBy(xpath = "//div[@class='col-12 lg:col-9 md:col-9']//img[@alt='country']")
    private WebElement selectcountry;

    @FindBy(xpath = "//p[contains(text(),'Samoa')]")
    private WebElement countryName;

    @FindBy(xpath = "//button[.//span[@class='p-button-label' and text()='Login']]")
    private WebElement LoginButton;

    @FindBy(xpath = "//input[@placeholder='Enter Mobile Number']")
    private WebElement EnterMobileNumber;

    @FindBy(xpath = "//input[@id='password']")
    private WebElement EnterPassword;

    @FindBy(xpath = "(//button[@type='button'])[1]")
    private WebElement LoginButtonClick;

    @FindBy(xpath = "//input[@autocomplete='one-time-code']")
    private List<WebElement> otpFields;

    @FindBy(xpath = "//button[@value='Submit']")
    private WebElement SubmitButton;

    @FindBy(xpath = "//button[@id='step2']")
    private WebElement SwitchAccount;

    @FindBy(xpath = "(//div[@class='main-list notAccountFav'])[1]")
    private WebElement AccountName;

    @FindBy(xpath = "(//p[@class='servicename'])[1]")
    private WebElement ServiceName;

    @FindBy(xpath = "//p[text()=' Emergency Loan ']")
    private WebElement EmergencyLoan;

    @FindBy(xpath = "//h1[text()='Eligible Loan Amount']")
    private WebElement EligibleLoanAmountScreen;

    @FindBy(xpath = "//p[text()=' Refresh Data Service ']")
    private WebElement RefreshDataService;

    @FindBy(xpath = "//span[text()='Reset']")
    private WebElement RefreshDataResetButton;

    @FindBy(xpath = "//span[text()='Back']")
    private WebElement BackButton;

    @FindBy(xpath = "//p[text()=' Purchase Device ']")
    private WebElement PurchaseDevice;

    @FindBy(xpath = "//p[text()=' FAQS ']")
    private WebElement FAQS;

    @FindBy(xpath = "(//button[@data-pc-name='button'])[5]")
    private WebElement FaqSection;

    @FindBy(xpath = "(//a[@role='button'])[1]")
    private WebElement FaqSectionDetail;

    @FindBy(xpath = "//p[text()='How can we help you?']")
    private WebElement FaqDetailPageHeadingVerification;

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // -------------------- Helper Methods --------------------
    private void safeClick(WebElement element) {
        int maxRetries = 3;
        for (int i = 0; i < maxRetries; i++) {
            try {
                wait.until(ExpectedConditions.visibilityOf(element));
                ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].scrollIntoView({behavior:'smooth', block:'center'});", element);
                wait.until(ExpectedConditions.elementToBeClickable(element));
                element.click();
                return;
            } catch (Exception e) {
                if (i == maxRetries - 1) throw e;
                try { Thread.sleep(500); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
            }
        }
    }

    // -------------------- Page Actions --------------------
    public void selectcountry() { safeClick(selectcountry); }
    public void countryName() { safeClick(countryName); }
    public void LoginButton() { safeClick(LoginButton); }
    public void LoginButtonClick() { safeClick(LoginButtonClick); }
    public void SwitchAccount() { safeClick(SwitchAccount); }
    public void AccountName() { safeClick(AccountName); }
    public void ServiceName() { safeClick(ServiceName); }
    public void EmergencyLoan() { safeClick(EmergencyLoan); }
    public void RefreshDataService() { safeClick(RefreshDataService); }
    public void RefreshDataResetButton() { safeClick(RefreshDataResetButton); }
    public void BackButton() { safeClick(BackButton); }
    public void PurchaseDevice() { safeClick(PurchaseDevice); }
    public void FAQS() { safeClick(FAQS); }
    public void FaqSection() { safeClick(FaqSection); }
    public void FaqSectionDetail() { safeClick(FaqSectionDetail); }

    public void EnterMobileNumber(String userid) {
        wait.until(ExpectedConditions.visibilityOf(EnterMobileNumber));
        EnterMobileNumber.clear();
        EnterMobileNumber.sendKeys(userid);
    }

    public void EnterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(EnterPassword));
        EnterPassword.clear();
        EnterPassword.sendKeys(password);
    }

    public void enterOTP(String otp) {
        wait.until(ExpectedConditions.visibilityOfAllElements(otpFields));
        otpFields.get(0).click();
        char[] digits = otp.toCharArray();
        for (int i = 0; i < digits.length && i < otpFields.size(); i++) {
            otpFields.get(i).sendKeys(String.valueOf(digits[i]));
        }
    }

    public void SubmitButton() { safeClick(SubmitButton); }

    public boolean isEligibleLoanAmountScreenDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(EligibleLoanAmountScreen));
            return EligibleLoanAmountScreen.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFaqDetailPageHeadingDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(FaqDetailPageHeadingVerification));
            System.out.println("FAQ Detail Page Heading: " + FaqDetailPageHeadingVerification.getText());
            return FaqDetailPageHeadingVerification.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void GetPurchaseDeviceTitle() {
        Iterator<String> it = driver.getWindowHandles().iterator();
        String parentWindow = it.next();
        if (it.hasNext()) {
            String childWindow = it.next();
            driver.switchTo().window(childWindow);
            System.out.println("Page Title: " + driver.getTitle());
            System.out.println("Current URL: " + driver.getCurrentUrl());
            driver.close();
            driver.switchTo().window(parentWindow);
        }
    }
}
