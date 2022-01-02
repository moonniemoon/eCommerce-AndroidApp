package com.example.ecommerce.models;

public class Wishlist {

    private String id, name, description, gender, size, colour, category, seller, imageUrl;
    private Integer quantity;
    private Double price;

    public Wishlist(){

    }

    public Wishlist(String id, String name, String description, String gender, String size, String colour, String category, String seller, String imageUrl, Integer quantity, Double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.gender = gender;
        this.size = size;
        this.colour = colour;
        this.category = category;
        this.seller = seller;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getsize() {
        return size;
    }

    public void setsize(String size) {
        this.size = size;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getquantity() {
        return quantity;
    }

    public void setquantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getprice() {
        return price;
    }

    public void setprice(Double price) {
        this.price = price;
    }
}
