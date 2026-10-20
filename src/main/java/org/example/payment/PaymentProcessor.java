package org.example.payment;

import org.example.config.AppConfig;
import org.example.fee.FeeStrategy;
import org.example.fee.StandardFee;
import org.example.model.Order;
import org.example.model.PaymentResult;
import org.example.validation.AmountLimitRule;
import org.example.validation.PaymentValidationRule;
import org.example.validation.SupportedCurrencyRule;

import java.util.List;
import java.util.Optional;

/**
 * Business logic of a payment. Works only with the abstract PaymentMethod,
 * so new payment methods do not require any change here.
 *
 * @author Karyna Litvinova
 */
public class PaymentProcessor {
    private final List<PaymentValidationRule> rules;

    public PaymentProcessor() {
        this(List.of(new SupportedCurrencyRule(), new AmountLimitRule()));
    }

    public PaymentProcessor(List<PaymentValidationRule> rules) {
        this.rules = List.copyOf(rules);
    }

    /** order total (with discount) + tax from AppConfig, then the fee strategy */
    public double calculateAmountToPay(Order order, FeeStrategy feeStrategy) {
        double withTax = order.calculateTotal() * (1 + AppConfig.getInstance().getTaxRate());
        double amount = feeStrategy.apply(withTax);
        return Math.round(amount * 100) / 100.0;
    }

    public PaymentResult process(Order order, PaymentMethod paymentMethod){
        return process(order, paymentMethod, new StandardFee());
    }

    public PaymentResult process(Order order, PaymentMethod paymentMethod, FeeStrategy feeStrategy){
        if (order == null || paymentMethod == null || feeStrategy == null) {
            return new PaymentResult(false, "Order, payment method and fee strategy are required.");
        }
        if (order.isPaid()) {
            return new PaymentResult(false, "Order is already paid.");
        }
        if (order.isEmpty()) {
            return new PaymentResult(false, "Cannot pay an empty order.");
        }

        double amount = calculateAmountToPay(order, feeStrategy);
        for (PaymentValidationRule rule : rules) {
            Optional<String> error = rule.validate(order, amount);
            if (error.isPresent()) {
                return new PaymentResult(false, error.get());
            }
        }

        PaymentResult result = paymentMethod.pay(amount, order.getCurrency());

        if(result.isSuccessful()){
            order.markAsPaid();
        }

        return result;
    }
}