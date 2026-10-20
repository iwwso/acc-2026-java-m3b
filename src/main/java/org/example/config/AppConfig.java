package org.example.config;

import java.util.List;

/**
 * Application configuration stored in a Singleton:
 * private constructor + one shared instance created on first use.
 *
 * @author Karyna Litvinova
 */
public final class AppConfig {
    private static AppConfig instance;

    private final String applicationName;
    private final String currency;
    private final double taxRate;
    private final List<String> supportedCurrencies;
    private final double maxTransactionAmount;
    private final double vipDiscountRate;
    private final double internationalFeeRate;

    private AppConfig(){
        this.applicationName = "Bootcamp Payment App";
        this.currency = "EUR";
        this.taxRate = 0.21;
        this.supportedCurrencies = List.of("EUR", "USD", "GBP");
        this.maxTransactionAmount = 10_000;
        this.vipDiscountRate = 0.05;
        this.internationalFeeRate = 0.03;
    }

    // synchronized: safe even if two threads ask for the instance at the same time
    public static synchronized AppConfig getInstance(){
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    public String getApplicationName() {
        return applicationName;
    }
    public String getCurrency() {
        return currency;
    }
    public double getTaxRate() {
        return taxRate;
    }
    public List<String> getSupportedCurrencies() {
        return supportedCurrencies;
    }
    public double getMaxTransactionAmount() {
        return maxTransactionAmount;
    }
    public double getVipDiscountRate() {
        return vipDiscountRate;
    }
    public double getInternationalFeeRate() {
        return internationalFeeRate;
    }
}