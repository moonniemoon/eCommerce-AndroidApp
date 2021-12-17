package com.example.ecommerce.models;

public class Boutiques {
    private String logo, background, cID, email, name, password, phone;

    public Boutiques() { }

    public Boutiques(String logo, String background, String cID, String email, String name, String password, String phone) {
        this.logo = logo;
        this.background = background;
        this.cID = cID;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getcID() {
        return cID;
    }

    public void setcID(String cID) {
        this.cID = cID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
