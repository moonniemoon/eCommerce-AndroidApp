package com.example.ecommerce.accounts;

public class Company {
    public String companyName, email, phone, password, companyLogoURL, backgroundURL, userType;
    public double revenue;

    public Company(){

    }

    public Company(String companyName, String email, String phone, String password, String companyLogoURL, String backgroundURL, double revenue, String userType) {
        this.companyName = companyName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.companyLogoURL = companyLogoURL;
        this.backgroundURL = backgroundURL;
        this.revenue = revenue;
        this.userType = userType;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
}
