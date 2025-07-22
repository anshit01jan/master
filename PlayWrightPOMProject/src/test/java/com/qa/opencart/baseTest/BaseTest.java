package com.qa.opencart.baseTest;

import com.microsoft.playwright.Page;
import com.qa.opencart.config.ConfigReader;
import com.qa.opencart.excelUtility.ExcelReader;
import com.qa.opencart.factory.PlayWrightFactory;
import com.qa.opencart.pages.AccountCreationSuccessPage;
import com.qa.opencart.pages.LoginPage;
import com.qa.opencart.pages.RegisterPage;
import com.qa.opencart.utils.utility;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import java.util.Properties;

public class BaseTest {
    PlayWrightFactory pf;
    ConfigReader cr;
    Page page;
    protected RegisterPage registerPage;
    protected LoginPage loginPage;
    protected ExcelReader ec;
    utility uc;
    
    @Parameters("browser")
    @BeforeTest
    public void setUp(String browser){
        pf = new PlayWrightFactory();
        cr = new ConfigReader();
        Properties p = cr.readConfigFile();
        page = pf.init_browser(browser, p.getProperty("url"));
        registerPage = new RegisterPage(page);
        loginPage = new LoginPage(page);
        ec = new ExcelReader();
        uc = new utility(page);
    }
    
    @AfterTest
    public void tearDown(){
        pf.closeBrowser();
    }
}
