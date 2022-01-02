package com.example.ecommerce.models;

public class Boutiques {

    private String backgroundURL, companyLogoURL, companyName, email, password, phone, userType;
    private Double revenue;

    public Boutiques(){

    }

    public Boutiques(String backgroundURL, String companyLogoURL, String companyName, String email, String password, String phone, String userType, Double revenue) {
        this.backgroundURL = backgroundURL;
        this.companyLogoURL = companyLogoURL;
        this.companyName = companyName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.userType = userType;
        this.revenue = revenue;
    }

    public String getCompanyLogoURL() {
        return companyLogoURL;
    }

    public void setCompanyLogoURL(String companyLogoURL) {
        this.companyLogoURL = companyLogoURL;
    }

    public String getBackgroundURL() {
        return backgroundURL;
    }

    public void setBackgroundURL(String backgroundURL) {
        this.backgroundURL = backgroundURL;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }
}
