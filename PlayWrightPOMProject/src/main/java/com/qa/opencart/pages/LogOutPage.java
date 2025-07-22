package com.qa.opencart.pages;

import com.microsoft.playwright.Page;
import com.qa.opencart.utils.ElementUtils;

public class LogOutPage extends ElementUtils {
    
    private Page page;

    private String msgContent = "#content h1";
    private String registerLink = ".list-group a:nth-of-type(2)";

    public LogOutPage(Page page){
        super(page);
        this.page = page;
    }


    public String getLogOutMessage(){
        return getTextContent(msgContent);
    }
    
    public RegisterPage clickRegisterLink(){
        clickOnElement(registerLink);
        return new RegisterPage(page);
    }

}
