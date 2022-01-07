package com.example.ecommerce.models;

public class Sell {
    private String sellTime, itemID, itemName;
    private Integer quantity;
    private Double price, totalIncome;

    public Sell(){

    }

    public Sell(String sellTime, String itemID, String itemName, Integer itemQuantity, Double price, Double totalIncome) {
        this.sellTime = sellTime;
        this.itemID = itemID;
        this.itemName = itemName;
        this.quantity = itemQuantity;
        this.price = price;
        this.totalIncome = totalIncome;
    }

    public String getSellTime() {
        return sellTime;
    }

    public void setSellTime(String sellTime) {
        this.sellTime = sellTime;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }
}
