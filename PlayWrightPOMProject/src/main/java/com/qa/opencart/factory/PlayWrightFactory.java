package com.qa.opencart.factory;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class PlayWrightFactory {
    Playwright playwright;
    Browser browser;
    BrowserContext bc;
    protected Page page;
    
    public Page init_browser(String browserName, String url){
        System.out.println("Browser is " + browserName);
        playwright = Playwright.create();
        switch (browserName.toLowerCase().trim()){
            case "chromium":
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
                break;
            case "firefox":
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
                break;
            case "webkit":
                browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
                break;
            case "chrome":
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
                break;
            default:
                System.out.println("Wrong browser selected " + browserName);
        }
        bc = browser.newContext();
        page = bc.newPage();
        page.navigate(url);
        return page;
    }
    
    public void closeBrowser(){
        bc.browser().close();
    }
    
}
