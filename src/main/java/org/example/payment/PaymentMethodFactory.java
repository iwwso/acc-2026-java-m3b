package org.example.payment;

/**
 * Factory: the only place where payment method objects are created.
 * A new payment method = new class + one new factory method.
 *
 * @author Karyna Litvinova
 */
public final class PaymentMethodFactory {

    private PaymentMethodFactory() {
        // static factory - no instances
    }

    public static PaymentMethod createCreditCardPayment(String cardNumber,String cardHolderName){
        return new CreditCardPayment(cardNumber,cardHolderName);
    }

    public static PaymentMethod createPaypalPayment(String email){
        return new PaypalPayment(email);
    }

    public static PaymentMethod createGiftCardPayment(String code, double balance){
        return new GiftCardPayment(code, balance);
    }
}