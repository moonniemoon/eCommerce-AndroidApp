package com.example.ecommerce.models;

public class Shipments {
    private String address, city, date, name, phone, userUID, status, paymentMethod, country, sellers;
    private Double totalAmount;

    public Shipments(){

    }

    public Shipments(String address, String city, String date, String name, String phone, String userUID, String status, String paymentMethod, String country, Double totalAmount, String sellers) {
        this.address = address;
        this.city = city;
        this.date = date;
        this.name = name;
        this.phone = phone;
        this.userUID = userUID;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.country = country;
        this.totalAmount = totalAmount;
        this.sellers = sellers;
    }

    public String getSellers() {
        return sellers;
    }

    public void setSellers(String sellers) {
        this.sellers = sellers;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
