package org.example.payment;

import org.example.model.PaymentResult;

import java.util.Optional;

public class GiftCardPayment extends PaymentMethod{
    private final String code;
    private double balance;

    public GiftCardPayment(String code, double balance) {
        super("GiftCard");
        this.code = code == null ? null : code.trim();
        this.balance = balance;
    }

    @Override
    protected Optional<String> validate() {
        if (isBlank(code)) {
            return Optional.of("Gift card code is missing.");
        }
        if (balance < 0) {
            return Optional.of("Gift card balance cannot be negative.");
        }
        return Optional.empty();
    }

    @Override
    protected PaymentResult processPayment(double amount, String currency){
        if (amount > balance) {
            return new PaymentResult(false, "Insufficient gift card balance: available "
                    + format(balance, currency) + ", required " + format(amount, currency) + ".");
        }
        balance -= amount;
        return new PaymentResult(true, "Paid " + format(amount, currency)
                + " using gift card " + code + ". Remaining balance: " + format(balance, currency));
    }

    public double getBalance() {
        return balance;
    }
}