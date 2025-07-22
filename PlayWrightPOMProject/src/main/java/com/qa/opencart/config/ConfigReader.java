package com.qa.opencart.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    
    Properties prop;
    
    public Properties readConfigFile(){
        try {
            FileInputStream fis = new FileInputStream(new File("src\\test\\resources\\config.properties"));
            prop = new Properties();
            prop.load(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return prop;
    }
    
}
