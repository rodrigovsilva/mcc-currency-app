package com.rvs.challenge.mcc.currency.service.currencyconverter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Available currencies api model.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvailableCurrencies {

    private boolean success;

    private Map<String, String> currencies;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Map<String, String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Map<String, String> currencies) {
        this.currencies = currencies;
    }
}
 
