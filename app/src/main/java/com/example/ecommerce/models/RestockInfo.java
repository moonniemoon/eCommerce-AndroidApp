package com.example.ecommerce.models;

public class RestockInfo {
    private String itemID, itemName, restockTime;
    private Integer quantity;
    private Double totalCost;

    RestockInfo(){

    }

    public RestockInfo(String itemID, String itemName, String restockTime,  Integer quantity, Double totalCost) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.restockTime = restockTime;
        this.quantity = quantity;
        this.totalCost = totalCost;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
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

    public String getRestockTime() {
        return restockTime;
    }

    public void setRestockTime(String restockTime) {
        this.restockTime = restockTime;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
