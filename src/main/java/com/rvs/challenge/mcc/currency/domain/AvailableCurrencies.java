package com.rvs.challenge.mcc.currency.domain;

public enum AvailableCurrencies {

    USD("USD"),
    EUR("EUR"),
    BRL("BRL");

    private String currency;

    AvailableCurrencies(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }
}
