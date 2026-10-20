package org.example.model;

import org.example.config.AppConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Order is created with the Builder pattern.
 *
 * @author Karyna Litvinova
 */
public class Order {
    private final String customerName;
    private final String currency;
    private final List<OrderItem> items;
    private OrderStatus status;
    private Discount discount = new NoDiscount();

    private Order(Builder builder) {
        this.customerName = builder.customerName;
        this.currency = builder.currency;
        this.items = new ArrayList<>(builder.items);
        this.status = OrderStatus.NEW;
    }

    public void addItem(OrderItem item){
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (status != OrderStatus.NEW) {
            throw new IllegalStateException("Cannot add items to an order with status " + status);
        }
        items.add(item);
    }

    public double getSubtotal(){
        return items.stream().mapToDouble(OrderItem::calculateTotal).sum();
    }

    public double calculateTotal(){
        // total of all items with the discount applied
        return Math.max(0, discount.apply(getSubtotal()));
    }

    public void markAsPaid(){
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot pay an empty order");
        }
        if (isPaid()) {
            throw new IllegalStateException("Order is already paid");
        }
        this.status = OrderStatus.PAID;
    }

    public void applyDiscount(Discount discount){
        if (discount == null) {
            throw new IllegalArgumentException("Discount cannot be null");
        }
        if (isPaid()) {
            throw new IllegalStateException("Cannot change discount of a paid order");
        }
        this.discount = discount;
    }

    public boolean isPaid(){
        return this.status == OrderStatus.PAID;
    }

    public boolean isEmpty(){
        return items.isEmpty();
    }

    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }
    public String getCustomerName() {
        return customerName;
    }
    public String getCurrency() {
        return currency;
    }
    public OrderStatus getStatus() {
        return status;
    }
    public Discount getDiscount() {
        return discount;
    }
    public static Builder builder(){
        return new Builder();
    }
    public static class Builder{
        private String customerName;
        private String currency = AppConfig.getInstance().getCurrency();
        private List<OrderItem> items = new ArrayList<>();
        public Builder customerName(String customerName){
            this.customerName = customerName;
            return this;
        }
        public Builder currency(String currency){
            this.currency = currency;
            return this;
        }
        public Builder addItem(OrderItem item){
            this.items.add(item);
            return this;
        }
        public Order build(){
            if (customerName == null || customerName.isBlank()) {
                throw new IllegalArgumentException("Customer name cannot be empty");
            }
            if (currency == null || currency.isBlank()) {
                throw new IllegalArgumentException("Currency cannot be empty");
            }
            customerName = customerName.trim();
            currency = currency.trim().toUpperCase();
            return new Order(this);
        }
    }
}