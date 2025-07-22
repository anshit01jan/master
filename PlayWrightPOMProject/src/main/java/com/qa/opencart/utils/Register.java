package com.qa.opencart.utils;

public class Register {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String telephone;
    private final String password;
    private final String confirmPassword;

    public Register(RegisterBuilder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.telephone = builder.telephone;
        this.password = builder.password;
        this.confirmPassword = builder.confirmPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    
    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
    

    public static class RegisterBuilder {
        private String firstName;
        private String lastName;
        private String email;
        private String telephone;
        private String password;
        private String confirmPassword;

        public RegisterBuilder(String firstName, String lastName, String email, String telephone, String password, String confirmPassword) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.telephone = telephone;
            this.password = password;
            this.confirmPassword = confirmPassword;
        }

        public RegisterBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public RegisterBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public RegisterBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public RegisterBuilder setTelephone(String telephone) {
            this.telephone = telephone;
            return this;
        }

        public RegisterBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public RegisterBuilder setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
            return this;
        }

        public Register build() {
            return new Register(this);
        }
    }
}

