package com.qa.opencart.utils;

import com.microsoft.playwright.Page;
import com.qa.opencart.factory.PlayWrightFactory;
import org.apache.commons.lang3.RandomStringUtils;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

public class utility {
    
    private static Page page;

    public utility(Page page) {
        utility.page = page;
    }

    public static String getRandomNum(){
        return new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
    }
    
    public static String generateRandomString(){
        return RandomStringUtils.randomAlphabetic(10);
    }
    
    public static String takeScreenshot(){
        String path = System.getProperty("user.dir")+"/screenshot/"+System.currentTimeMillis()+".png";
        byte[] buffer = page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(path)).setFullPage(true));
        return Base64.getEncoder().encodeToString(buffer);
    }
}
