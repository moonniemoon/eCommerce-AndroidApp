package com.example.ecommerce.models;

public class OrderedProduct {
    private String productID, name, seller, size, status, image, date;
    private Integer quantity;
    private Double price;

    public OrderedProduct(){

    }

    public OrderedProduct(String productID, String name, String seller, String size, String status, String image, Integer quantity, Double price, String date) {
        this.productID = productID;
        this.name = name;
        this.seller = seller;
        this.size = size;
        this.status = status;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
