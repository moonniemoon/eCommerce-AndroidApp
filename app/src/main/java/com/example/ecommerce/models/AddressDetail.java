package com.example.ecommerce.models;

public class AddressDetail {

    private String firstName, lastName, phone, townCity, address, postcode, country, shortcutName;

    public AddressDetail(){

    }

    public AddressDetail(String firstName, String lastName, String phone, String townCity, String address, String postcode, String country, String shortcutName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.townCity = townCity;
        this.address = address;
        this.postcode = postcode;
        this.country = country;
        this.shortcutName = shortcutName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTownCity() {
        return townCity;
    }

    public void setTownCity(String townCity) {
        this.townCity = townCity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getShortcutName() {
        return shortcutName;
    }

    public void setShortcutName(String shortcutName) {
        this.shortcutName = shortcutName;
    }
}
