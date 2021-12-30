package com.example.ecommerce.models;

public class Item {
    private String ID, name, description, gender, size, colour, category, seller, imageUrl;
    private Integer quantity;
    private Double price;
    private Boolean duplicateItems;

    public Item(){

    }

    public Item(String ID, String name, String description, String gender, String size, String colour, Integer quantity, String category, String seller, String imageUrl, Double price, Boolean duplicateItems) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.gender = gender;
        this.size = size;
        this.colour = colour;
        this.quantity = quantity;
        this.category = category;
        this.seller = seller;
        this.imageUrl = imageUrl;
        this.price = price;
        this.duplicateItems = duplicateItems;
    }


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getDuplicateItems() {
        return duplicateItems;
    }

    public void setDuplicateItems(Boolean duplicateItems) {
        this.duplicateItems = duplicateItems;
    }
}
