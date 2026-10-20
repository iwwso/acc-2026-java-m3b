package org.example.menu;

import org.example.config.AppConfig;
import org.example.model.FixedAmountDiscount;
import org.example.model.NoDiscount;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.PaymentResult;
import org.example.model.PercentageDiscount;
import org.example.payment.PaymentMethod;
import org.example.payment.PaymentMethodFactory;
import org.example.payment.PaymentProcessor;

import java.util.Scanner;

/**
 * Console user interface.
 *
 * @author Karyna Litvinova
 */
public class ConsoleMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final PaymentProcessor paymentProcessor = new PaymentProcessor();

    private Order currentOrder;

    public void start(){
        AppConfig config = AppConfig.getInstance();
        System.out.println("Welcome to " + config.getApplicationName());

        boolean running = true;
        while(running){
            printMenu();

            int option = readInt("Choose an option: ");

            switch (option){
                case 1 -> createOrder();
                case 2 -> addItem();
                case 3 -> viewOrder();
                case 4 -> applyDiscount();
                case 5 -> payOrder();
                case 0 -> running = false;
                default -> System.out.println("Invalid option");
            }
            System.out.println();
        }
        System.out.println("Goodbye!");
    }

    private void createOrder(){
        String customerName = readNonEmpty("Customer name: ");

        try {
            currentOrder = Order.builder()
                    .customerName(customerName)
                    .build();
            System.out.println("Order created for " + currentOrder.getCustomerName() + " in " + currentOrder.getCurrency());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void addItem(){
        if (!hasOpenOrder()) {
            return;
        }

        String itemName = readNonEmpty("Item name: ");
        double price = readPositiveDouble("Price: ");
        int quantity = readPositiveInt("Quantity: ");

        try {
            currentOrder.addItem(new OrderItem(itemName, price, quantity));
            System.out.println("Item added to order");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void viewOrder(){
        if (!hasOrder()) {
            return;
        }
        String currency = currentOrder.getCurrency();

        System.out.println("Customer: " + currentOrder.getCustomerName());
        System.out.println("Status: " +  currentOrder.getStatus());
        System.out.println("Items:");
        if (currentOrder.isEmpty()) {
            System.out.println("- (no items yet)");
        }
        for (OrderItem item : currentOrder.getItems()){
            System.out.println("- " + item);
        }

        System.out.printf("Subtotal: %.2f %s%n", currentOrder.getSubtotal(), currency);
        System.out.println("Discount: " + currentOrder.getDiscount().getCode());
        System.out.printf("Total: %.2f %s%n", currentOrder.calculateTotal(), currency);
    }

    private void applyDiscount(){
        if (!hasOpenOrder()) {
            return;
        }
        System.out.println("""
                Select discount:
                1. Percentage discount
                2. Fixed amount discount
                3. Remove discount
                """);
        int option = readInt("Choose an option: ");

        try {
            switch (option) {
                case 1 -> {
                    double percentage = readPositiveDouble("Percentage (1-100): ");
                    currentOrder.applyDiscount(new PercentageDiscount("PERCENT-" + percentage, percentage));
                }
                case 2 -> {
                    double amount = readPositiveDouble("Amount: ");
                    currentOrder.applyDiscount(new FixedAmountDiscount("FIXED-" + amount, amount));
                }
                case 3 -> currentOrder.applyDiscount(new NoDiscount());
                default -> {
                    System.out.println("Invalid discount option");
                    return;
                }
            }
            System.out.printf("Discount applied. New total: %.2f %s%n",
                    currentOrder.calculateTotal(), currentOrder.getCurrency());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void payOrder(){
        if (!hasOpenOrder()) {
            return;
        }
        if (currentOrder.isEmpty()) {
            System.out.println("Add at least one item before paying.");
            return;
        }

        System.out.println("""
                Select payment method:
                1. Credit Card
                2. PayPal
                3. Gift Card
                """);
        int option = readInt("Choose an option: ");

        PaymentMethod paymentMethod = switch(option){
            case 1 -> createCreditCardPayment();
            case 2 -> createPaypalPayment();
            case 3 -> createGiftCardPayment();
            default -> null;
        };
        if (paymentMethod == null) {
            System.out.println("Invalid payment method");
            return;
        }

        PaymentResult result = paymentProcessor.process(currentOrder, paymentMethod);
        System.out.println(result.getMessage());
    }

    private PaymentMethod createCreditCardPayment(){
        String cardNumber = readNonEmpty("Card number: ");
        String cardHolderName = readNonEmpty("Card holder name: ");
        return PaymentMethodFactory.createCreditCardPayment(cardNumber,cardHolderName);
    }

    private  PaymentMethod createPaypalPayment(){
        String email = readNonEmpty("PayPal email: ");
        return PaymentMethodFactory.createPaypalPayment(email);
    }

    private PaymentMethod createGiftCardPayment(){
        String code = readNonEmpty("Gift card code: ");
        double balance = readNonNegativeDouble("Available balance: ");
        return PaymentMethodFactory.createGiftCardPayment(code, balance);
    }

    private boolean hasOrder(){
        if (currentOrder == null) {
            System.out.println("No order yet. Create an order first (option 1).");
            return false;
        }
        return true;
    }

    private boolean hasOpenOrder(){
        if (!hasOrder()) {
            return false;
        }
        if (currentOrder.isPaid()) {
            System.out.println("This order is already paid. Create a new order (option 1).");
            return false;
        }
        return true;
    }

    private void printMenu(){
        System.out.println("""
                1. Create order
                2. Add item to order
                3. View order
                4. Apply discount
                5. Pay order
                0. Exit
                """);
    }

    private String readNonEmpty(String prompt){
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Value cannot be empty.");
        }
    }

    private int readInt(String prompt){
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private int readPositiveInt(String prompt){
        while (true) {
            int value = readInt(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Value must be greater than zero.");
        }
    }

    private double readDouble(String prompt){
        while (true) {
            System.out.print(prompt);
            try {
                // accept both 9.99 and 9,99
                return Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number, for example 9.99");
            }
        }
    }

    private double readPositiveDouble(String prompt){
        while (true) {
            double value = readDouble(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Value must be greater than zero.");
        }
    }

    private double readNonNegativeDouble(String prompt){
        while (true) {
            double value = readDouble(prompt);
            if (value >= 0) {
                return value;
            }
            System.out.println("Value cannot be negative.");
        }
    }
}