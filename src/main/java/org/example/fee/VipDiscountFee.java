package org.example.fee;

import org.example.config.AppConfig;

/**
 * @author Karyna Litvinova
 */
public class VipDiscountFee implements FeeStrategy {
    @Override
    public double apply(double amount) {
        return amount * (1 - AppConfig.getInstance().getVipDiscountRate());
    }

    @Override
    public String getDescription() {
        return String.format("VIP customer discount -%.0f%%", AppConfig.getInstance().getVipDiscountRate() * 100);
    }
}