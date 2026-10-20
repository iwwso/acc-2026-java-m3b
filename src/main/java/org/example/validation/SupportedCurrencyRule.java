package org.example.validation;

import org.example.config.AppConfig;
import org.example.model.Order;

import java.util.List;
import java.util.Optional;

/**
 * @author Karyna Litvinova
 */
public class SupportedCurrencyRule implements PaymentValidationRule {
    @Override
    public Optional<String> validate(Order order, double amount) {
        List<String> supported = AppConfig.getInstance().getSupportedCurrencies();
        if (!supported.contains(order.getCurrency())) {
            return Optional.of("Currency " + order.getCurrency() + " is not supported. Supported: " + supported);
        }
        return Optional.empty();
    }
}