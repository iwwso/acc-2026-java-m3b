package org.example.payment;

import org.example.model.PaymentResult;

import java.util.Optional;

public class CreditCardPayment extends PaymentMethod {
    private final String cardNumber;
    private final String cardHolderName;

    public CreditCardPayment(String cardNumber, String cardHolderName) {
        super("CreditCard");
        // spaces are allowed when typing: "4111 1111 1111 1111"
        this.cardNumber = cardNumber == null ? null : cardNumber.replace(" ", "");
        this.cardHolderName = cardHolderName;
    }

    @Override
    protected Optional<String> validate() {
        if (isBlank(cardNumber)) {
            return Optional.of("Card number is missing.");
        }
        if (!cardNumber.matches("\\d{13,19}")) {
            return Optional.of("Card number must contain 13-19 digits.");
        }
        if (isBlank(cardHolderName)) {
            return Optional.of("Card holder name is missing.");
        }
        return Optional.empty();
    }

    @Override
    protected PaymentResult processPayment(double amount, String currency) {
        return new PaymentResult(true, "Paid " + format(amount, currency)
                + " using credit card ending with " + cardNumber.substring(cardNumber.length() - 4));
    }
}