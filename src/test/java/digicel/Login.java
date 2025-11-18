package digicel;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.digicel.test.utils.EmailUtils;
import javax.mail.Store;

import basepackage.Base;
import digicel.pages.LoginPage;

@Test(timeOut = 60000)
public class Login extends Base {

    private LoginPage lp;

    @BeforeClass
    public void setUpClass() throws Exception {
        // Use the driver initialized in Base
        lp = new LoginPage(driver);
    }

    public void SelectCountryAndLogin() throws Exception {
        // Select country and login
        lp.selectcountry();
        lp.countryName();
        lp.LoginButton();
        lp.EnterMobileNumber(prop.getProperty("userid"));
        lp.EnterPassword(prop.getProperty("password"));
        lp.LoginButtonClick();

        // Wait for OTP email
        EmailUtils emailUtils = new EmailUtils();

        // Validate email properties
        if (prop.getProperty("emailHost") == null || prop.getProperty("emailUser") == null
                || prop.getProperty("emailPassword") == null) {
            throw new RuntimeException("Email properties are not properly configured in propertyfile.properties");
        }

        Store store = emailUtils.connectToGmail(prop);
        List<String> emailText = null;
        String otp = null;

        // Retry logic to wait for email
        Pattern pattern = Pattern.compile("\\b(\\d{6})\\b");
        int retries = 5;
        for (int i = 0; i < retries; i++) {
            emailText = emailUtils.getUnreadMessageByFromEmail(store, "Inbox", "idam@digicelpacific.com",
                    "access code");
            if (!emailText.isEmpty()) {
                Matcher matcher = pattern.matcher(emailText.get(0));
                if (matcher.find()) {
                    otp = matcher.group(1);
                    break;
                }
            }
            Thread.sleep(5000); // wait 5 seconds before retry
        }

        if (otp == null) {
            throw new RuntimeException("OTP not received after retries.");
        }

        System.out.println("OTP is: " + otp);
        lp.enterOTP(otp);
        lp.SubmitButton();
    }

    public void SwitchAccount() {
        lp.SwitchAccount();
        lp.AccountName();
        lp.ServiceName();
    }

    public void RefreshDataService() {
        lp.RefreshDataService();
        lp.RefreshDataResetButton();
    }

    public void EmergencyLoan() {
        lp.EmergencyLoan();
        if (lp.isEligibleLoanAmountScreenDisplayed()) {
            System.out.println("Eligible Loan Amount screen displayed.");
        }
        lp.BackButton();
    }

    public void PurchaseDevice() {
        lp.PurchaseDevice();
        lp.GetPurchaseDeviceTitle();
    }

    public void FAQS() {
        lp.FAQS();
        lp.FaqSection();
        lp.FaqSectionDetail();
        if (lp.isFaqDetailPageHeadingDisplayed()) {
            System.out.println("FAQ Detail page heading displayed correctly.");
        }
        lp.BackButton();
    }

}