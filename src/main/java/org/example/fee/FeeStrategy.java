package org.example.fee;

/**
 * Strategy pattern: how the final amount is adjusted. discount or extra fee
 *
 * @author Karyna Litvinova
 */
public interface FeeStrategy {
    double apply(double amount);

    String getDescription();
}