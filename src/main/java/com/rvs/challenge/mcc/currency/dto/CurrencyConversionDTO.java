package com.rvs.challenge.mcc.currency.dto;

import java.util.Calendar;
import java.util.List;

/**
 * CurrencyConversion Result data transfer object.
 */
public class CurrencyConversionDTO {

    private String exchange;

    private Calendar timestamp;

    private List<ExchangeRateDTO> exchangeRates;

    public CurrencyConversionDTO(String exchange, Calendar timestamp, List<ExchangeRateDTO> exchangeRates) {
        this.exchange = exchange;
        this.timestamp = timestamp;
        this.exchangeRates = exchangeRates;
    }

    public CurrencyConversionDTO() {
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public List<ExchangeRateDTO> getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(List<ExchangeRateDTO> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }
}

