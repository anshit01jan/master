package com.qa.opencart.pages;

import com.microsoft.playwright.Page;
import com.qa.opencart.utils.ElementUtils;

public class AccountCreationSuccessPage extends ElementUtils {
    
    private Page page;

    private String msgContent = "#content h1";
    private String continueBtn = ".buttons .pull-right a";
    private String logout = "//div[@class='list-group']//a[text()='Logout']";

    public AccountCreationSuccessPage(Page page) {
        
        super(page);
        this.page = page;
    }

    public String getRegistrationSuccessMessage(){
        
        return getTextContent(msgContent);
    }
    
    public AccountPage clickContinueBtn(){
        clickOnElement(continueBtn);
        return new AccountPage(page);
    }
    
    public LogOutPage clickLogOut(){
        clickOnElement(logout);
        return new LogOutPage(page);
    }

}
