package org.example.validation;

import org.example.config.AppConfig;
import org.example.model.Order;

import java.util.Optional;

/**
 * @author Karyna Litvinova
 */
public class AmountLimitRule implements PaymentValidationRule {
    @Override
    public Optional<String> validate(Order order, double amount) {
        double max = AppConfig.getInstance().getMaxTransactionAmount();
        if (amount <= 0) {
            return Optional.of("Amount must be greater than zero.");
        }
        if (amount > max) {
            return Optional.of(String.format("Amount %.2f exceeds the transaction limit of %.2f.", amount, max));
        }
        return Optional.empty();
    }
}