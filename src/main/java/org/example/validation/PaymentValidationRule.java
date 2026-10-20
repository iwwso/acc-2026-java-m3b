package org.example.validation;

import org.example.model.Order;

import java.util.Optional;

/**
 * One validation rule checked before a payment
 * New rules can be added without changing PaymentProcessor
 *
 * @author Karyna Litvinova
 */
public interface PaymentValidationRule {
    /** @return error message if the payment is not allowed */
    Optional<String> validate(Order order, double amount);
}