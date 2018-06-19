package com.rvs.challenge.mcc.currency.domain;

/**
 * All available currencies
 */
public enum AvailableCurrencies {

    USD("USD"),
    EUR("EUR"),
    BRL("BRL");

    /**
     * Currency.
     */
    private String currency;

    AvailableCurrencies(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }
}
