package com.qa.opencart.tests;

import com.qa.opencart.baseTest.BaseTest;
import com.qa.opencart.pages.AccountPage;
import com.qa.opencart.utils.EnumStrings;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.qa.opencart.tests.AddRegisterPageTest.email;
import static com.qa.opencart.tests.AddRegisterPageTest.password;

public class LoginPageTest extends BaseTest {
    
    @Test(priority = 1)
    public void verifyLoginLinkDisplayed(){
        Assert.assertTrue(loginPage.getLoginLink(), "Verify Login Link is displayed");
    }
    
    @Test(priority = 2)
    public void verifyForgotPasswordLinkDisplayed(){
        Assert.assertTrue(loginPage.getForgotPasswordLink(), "Verify Forgot Password Link is displayed");   
    }
    
    @Test(priority = 3)
    public void verifySuccessfulLogin(){
        AccountPage accountPage = loginPage.doLogin(email,password);
        System.out.println(accountPage.getAccountMessageText());
        Assert.assertEquals(accountPage.getAccountMessageText(),"My Account");
        
    }
}
