package com.qa.opencart.pages;

import com.microsoft.playwright.Page;
import com.qa.opencart.utils.ElementUtils;
import com.qa.opencart.utils.Register;

public class RegisterPage extends ElementUtils {
    private Page page;
    
    private String firstName = "#input-firstname";
    private String lastName = "#input-lastname";
    private String email = "#input-email";
    private String telephone = "#input-telephone";
    private String password = "#input-password";
    private String confirmPassword = "#input-confirm";
    private String checkbox = "input[type='checkbox']";
    private String continueBtn = "input[type='submit']";
    private String registerLink = ".list-group a:nth-of-type(2)";
    private String msgContent = "#content h1";

    public RegisterPage(Page page) {
        super(page);
        this.page = page;
    }

    public RegisterPage clickOnRegisterLink(){
        clickOnElement(registerLink);
        return new RegisterPage(page);
    }
    
    public String getRegisterPageUrl(){
        return getPageUrl();
    }
    
    public void clickOnCheckboxOnRegisterPage(){
        clickOnElement(checkbox);
    }
    
    public void clickOnContinueButton(){
        clickOnElement(continueBtn);
    }
    
    public AccountCreationSuccessPage singleUserRegisterUser(Register user){
        fillValueInTextBox(firstName, user.getFirstName());
        fillValueInTextBox(lastName, user.getLastName());
        fillValueInTextBox(email, user.getEmail());
        fillValueInTextBox(telephone,user.getTelephone());
        fillValueInTextBox(password, user.getPassword());
        fillValueInTextBox(confirmPassword,user.getConfirmPassword());
        
        clickOnCheckboxOnRegisterPage();
        clickOnContinueButton();
        return new AccountCreationSuccessPage(page);
    }
    
    public String getRegisterHeader(){
        clickOnElement(registerLink);
        return getTextContent(msgContent);
    }
}
