package com.qa.opencart.tests;

import com.qa.opencart.baseTest.BaseTest;
import com.qa.opencart.pages.AccountCreationSuccessPage;
import com.qa.opencart.pages.LogOutPage;
import com.qa.opencart.utils.Constants;
import com.qa.opencart.utils.Register;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

import static com.qa.opencart.utils.utility.*;

public class AddRegisterPageTest extends BaseTest {

    AccountCreationSuccessPage accountCreationSuccessPage;
    LogOutPage logOutPage;
    protected static String email;
    protected static String password;
    
    @Test(priority = 1)
    public void verifyRegisterPageHeaderText(){
        String registerHeader = registerPage.getRegisterHeader();
        System.out.println(registerHeader);
        Assert.assertEquals(registerHeader,"Register Account");
    }
    
    @Test(priority = 2)
    public void verifyRegisterUserTest(){
       List<HashMap<String,Object>> excelReadMap =  ec.readExcel(Constants.FILE_PATH,Constants.SHEET_NAME);
       email = getRandomNum().concat("@gmail.com");
        password = generateRandomString().concat("1");
        registerPage = registerPage.clickOnRegisterLink();
                accountCreationSuccessPage = registerPage.singleUserRegisterUser(new Register.RegisterBuilder(
                        excelReadMap.get(0).get("firstName").toString(), excelReadMap.get(0).get("LastName").toString(),
                        email, excelReadMap.get(0).get("telephone").toString(),
                        password, password
                ).build());
        Assert.assertEquals(accountCreationSuccessPage.getRegistrationSuccessMessage(),"Your Account Has Been Created!");
    }

    @Test(priority = 3)
    public void verifyMultipleRegisterUserTest(){
        List<HashMap<String,Object>> excelReadMap =  ec.readExcel(Constants.FILE_PATH,Constants.SHEET_NAME);
        registerPage = registerPage.clickOnRegisterLink();
        for(int i=0; i<excelReadMap.size(); i++) {
            email = getRandomNum().concat("@gmail.com");
            password = generateRandomString().concat("1");
            accountCreationSuccessPage = registerPage.singleUserRegisterUser(new Register.RegisterBuilder(
                    excelReadMap.get(i).get("firstName").toString(), excelReadMap.get(i).get("LastName").toString(),
                    email, excelReadMap.get(i).get("telephone").toString(),
                    password, password
            ).build());
            Assert.assertEquals(accountCreationSuccessPage.getRegistrationSuccessMessage(),"Your Account Has Been Created!");
            logOutPage = accountCreationSuccessPage.clickLogOut();
            Assert.assertEquals(logOutPage.getLogOutMessage(),"Account Logout");
            registerPage = logOutPage.clickRegisterLink();
        }
    }
}
