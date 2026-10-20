package org.example.fee;

import org.example.config.AppConfig;

/**
 * @author Karyna Litvinova
 */
public class InternationalFee implements FeeStrategy {
    @Override
    public double apply(double amount) {
        return amount * (1 + AppConfig.getInstance().getInternationalFeeRate());
    }

    @Override
    public String getDescription() {
        return String.format("International transaction fee +%.0f%%", AppConfig.getInstance().getInternationalFeeRate() * 100);
    }
}