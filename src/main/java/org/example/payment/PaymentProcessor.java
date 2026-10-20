package org.example.payment;

import org.example.model.Order;
import org.example.model.PaymentResult;

/**
 * Business logic of a payment. Works only with the abstract PaymentMethod,
 * so new payment methods do not require any change here.
 *
 * @author Karyna Litvinova
 */
public class PaymentProcessor {
    public PaymentResult process(Order order, PaymentMethod paymentMethod){
        if (order == null || paymentMethod == null) {
            return new PaymentResult(false, "Order and payment method are required.");
        }
        if (order.isPaid()) {
            return new PaymentResult(false, "Order is already paid.");
        }
        if (order.isEmpty()) {
            return new PaymentResult(false, "Cannot pay an empty order.");
        }

        PaymentResult result = paymentMethod.pay(order.calculateTotal(), order.getCurrency());

        if(result.isSuccessful()){
            order.markAsPaid();
        }

        return result;
    }
}