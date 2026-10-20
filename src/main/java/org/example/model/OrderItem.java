package org.example.model;

public class OrderItem {
    private final String name;
    private final double price;
    private final int quantity;

    public OrderItem(String name, double price, int quantity) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Item name cannot be empty");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        this.name = name.trim();
        this.price = price;
        this.quantity = quantity;
    }

    public double calculateTotal(){
        return price * quantity;
    }

    @Override
    public String toString(){
        return name + " x" + quantity + " = " + String.format("%.2f", calculateTotal());
    }
}