package org.example.model;

/**
 * Discount that subtracts a fixed amount (never below zero).
 *
 * @author Karyna Litvinova
 */
public class FixedAmountDiscount extends Discount {
    private final double amount;

    public FixedAmountDiscount(String code, double amount) {
        super(code);
        if (amount <= 0) {
            throw new IllegalArgumentException("Discount amount must be greater than zero");
        }
        this.amount = amount;
    }

    @Override
    public double apply(double originalAmount) {
        return Math.max(0, originalAmount - amount);
    }
}