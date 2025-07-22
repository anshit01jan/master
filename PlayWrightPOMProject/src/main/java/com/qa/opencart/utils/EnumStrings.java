package com.qa.opencart.utils;

public enum EnumStrings {
    
    PASSWORD("Anshit123"),
    CONFIRM_PASSWORD("Anshit123");

    private final String password;

    EnumStrings(String password) {
        this.password = password;
    }
    
    public String getPassword(){
        return password;
    }
}
