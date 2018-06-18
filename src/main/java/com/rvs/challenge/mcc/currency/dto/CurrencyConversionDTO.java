package com.rvs.challenge.mcc.currency.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * CurrencyConversion Result data transfer object.
 */
public class CurrencyConversionDTO {

    private String exchangeFrom;

    @DateTimeFormat(pattern="dd/MM/yyyy")
    private Calendar timestamp;

    private String exchangeTo;

    private BigDecimal rate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Calendar createdAt;

    public CurrencyConversionDTO() {
    }

    public CurrencyConversionDTO(String exchangeFrom, Calendar timestamp, String exchangeTo, BigDecimal rate, Calendar createdAt) {
        this.exchangeFrom = exchangeFrom;
        this.timestamp = timestamp;
        this.exchangeTo = exchangeTo;
        this.rate = rate;
        this.createdAt = createdAt;
    }

    public String getExchangeFrom() {
        return exchangeFrom;
    }

    public void setExchangeFrom(String exchangeFrom) {
        this.exchangeFrom = exchangeFrom;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Calendar timestamp) {
        this.timestamp = timestamp;
    }

    public String getExchangeTo() {
        return exchangeTo;
    }

    public void setExchangeTo(String exchangeTo) {
        this.exchangeTo = exchangeTo;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Calendar getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Calendar createdAt) {
        this.createdAt = createdAt;
    }
}


