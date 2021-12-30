package com.example.ecommerce.models;

import java.util.ArrayList;
import java.util.List;

public class ShoppingBag {
    private List<Item> items = new ArrayList<>();
    private String status, userID, orderTime;
    private Double total;
}
