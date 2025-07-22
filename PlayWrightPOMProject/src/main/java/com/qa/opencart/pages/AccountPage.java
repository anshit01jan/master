package com.qa.opencart.pages;

import com.microsoft.playwright.Page;
import com.qa.opencart.utils.ElementUtils;

public class AccountPage extends ElementUtils {
    
    private Page page;
    
    public AccountPage(Page page) {
        super(page);
    }

    private String accountMsgText = "//h2[text()='My Account']";
    
    public String getAccountMessageText(){
        return getTextContent(accountMsgText);
    }
}
