package com.rvs.challenge.mcc.currency.service.currencyconverter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.Map;

/**
 * CurrencyConversion rates model of a currency.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversionRates {

    private Boolean success;
    private Boolean historical;
    private String date;
    private String source;
    private Long timestamp;

    private ConversionRatesError error;

    private Map<String, BigDecimal> quotes;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getHistorical() {
        return historical;
    }

    public void setHistorical(Boolean historical) {
        this.historical = historical;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public ConversionRatesError getError() {
        return error;
    }

    public void setError(ConversionRatesError error) {
        this.error = error;
    }

    public Map<String, BigDecimal> getQuotes() {
        return quotes;
    }

    public void setQuotes(Map<String, BigDecimal> quotes) {
        this.quotes = quotes;
    }
}
 
