package org.example.fee;

/**
 * @author Karyna Litvinova
 */
public class StandardFee implements FeeStrategy {
    @Override
    public double apply(double amount) {
        return amount;
    }

    @Override
    public String getDescription() {
        return "Standard (no extra fee)";
    }
}