package org.example.model;

public class NoDiscount extends Discount {
    public NoDiscount() {
        super("NONE");
    }
    @Override
    public double apply(double originalAmount) {
        // no discount:the amount stays the same
        return originalAmount;
    }
}