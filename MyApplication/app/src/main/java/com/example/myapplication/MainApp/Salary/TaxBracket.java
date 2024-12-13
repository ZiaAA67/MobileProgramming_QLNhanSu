package com.example.myapplication.MainApp.Salary;

public enum TaxBracket {
    BRACKET1(0, 5_000_000, 0.05, 0),
    BRACKET2(5_000_001, 10_000_000, 0.10, 250_000),
    BRACKET3(10_000_001, 18_000_000, 0.15, 750_000),
    BRACKET4(18_000_001, 32_000_000, 0.20, 1_650_000),
    BRACKET5(32_000_001, 52_000_000, 0.25, 3_250_000),
    BRACKET6(52_000_001, 80_000_000, 0.30, 5_850_000),
    BRACKET7(80_000_001, Double.MAX_VALUE, 0.35, 9_850_000);

    private final double lowerLimit;
    private final double upperLimit;
    private final double taxRate;
    private final double deduction;

    TaxBracket(double lowerLimit, double upperLimit, double taxRate, double deduction) {
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.taxRate = taxRate;
        this.deduction = deduction;
    }

    public boolean isInBracket(double income) {
        return income > lowerLimit && income <= upperLimit;
    }

    public double calculateTax(double income) {
        return income * taxRate - deduction;
    }

    public static double getTax(double income) {
        for (TaxBracket bracket : values()) {
            if (bracket.isInBracket(income)) {
                return bracket.calculateTax(income);
            }
        }
        return 0;
    }
}
