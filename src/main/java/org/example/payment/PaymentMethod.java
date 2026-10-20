package org.example.payment;

import org.example.model.PaymentResult;

import java.util.Optional;

/**
 * Base class of all payment methods. Template Method pattern:
 * pay() does the common checks, subclasses only validate their own
 * fields and do the actual payment.
 *
 * @author Karyna Litvinova
 */
public abstract class PaymentMethod {
    private final String providerName;
    protected PaymentMethod(String providerName){
        this.providerName = providerName;
    }

    public final PaymentResult pay(double amount, String currency){
        if (amount <= 0){
            return new PaymentResult(false, "Amount must be greater than zero.");
        }
        Optional<String> error = validate();
        if (error.isPresent()) {
            return new PaymentResult(false, error.get());
        }
        return processPayment(amount, currency);
    }

    /** @return error message if payment details are missing or invalid */
    protected abstract Optional<String> validate();

    protected abstract PaymentResult processPayment(double amount, String currency);

    public String getProviderName() {
        return providerName;
    }

    protected static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    protected static String format(double amount, String currency) {
        return String.format("%.2f %s", amount, currency);
    }
}