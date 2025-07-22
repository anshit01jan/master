package com.qa.opencart.pages;

import com.microsoft.playwright.Page;
import com.qa.opencart.utils.ElementUtils;
public class LoginPage extends ElementUtils {
    
    private Page page;
    
    private String username = "#input-email";
    private String password = "#input-password";
    private String loginBtn = "input[type='submit']";
    private String ForgotPwdLink = ".form-group a";
    private String loginLink = "//ul[@class='breadcrumb']//a[text()='Login']";


    public LoginPage(Page page) {
        super(page);
        this.page = page;
    }
    
    public boolean getLoginLink(){
        return getVisibleLink(loginLink);
    }
    
    public AccountPage doLogin(String user, String pass){
        page.fill(username,user);
        page.fill(password,pass);
        page.click(loginBtn);
        return new AccountPage(page);
    }
    
    public boolean getForgotPasswordLink(){
        return getVisibleLink(ForgotPwdLink);
    }
}
