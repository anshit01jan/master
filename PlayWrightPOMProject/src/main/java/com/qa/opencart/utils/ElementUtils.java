package com.qa.opencart.utils;

import com.microsoft.playwright.Page;

public class ElementUtils {
    
    Page page;
    
    public ElementUtils(Page page){
        this.page = page;
    }
    
    public void fillValueInTextBox(String locator, String value){
        page.fill(locator, value);
    }
    
    public String getTextContent(String locator){
        return page.textContent(locator);
    }
    
    public void clickOnElement(String locator){
        page.locator(locator).first().click();
    }
    
    public String getPageTitle(){
        return page.title();
    }
    
    public String getPageUrl(){
        return page.url();
    }
    
    public boolean getVisibleLink(String locator){
        return page.isVisible(locator);
    }
    
}
