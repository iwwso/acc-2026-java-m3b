package org.example.payment;

import org.example.model.PaymentResult;

import java.util.Optional;

public class PaypalPayment extends PaymentMethod {
    private static final String EMAIL_PATTERN = "^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$";

    private final String email;

    public PaypalPayment(String email) {
        super("PayPal");
        this.email = email == null ? null : email.trim();
    }

    @Override
    protected Optional<String> validate() {
        if (isBlank(email)) {
            return Optional.of("PayPal email is missing.");
        }
        if (!email.matches(EMAIL_PATTERN)) {
            return Optional.of("PayPal email is not valid: " + email);
        }
        return Optional.empty();
    }

    @Override
    protected PaymentResult processPayment(double amount, String currency) {
        return new PaymentResult(true, "Paid " + format(amount, currency) + " using PayPal (" + email + ")");
    }
}