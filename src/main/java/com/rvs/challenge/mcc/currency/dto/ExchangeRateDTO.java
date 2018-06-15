package com.rvs.challenge.mcc.currency.dto;

import java.math.BigDecimal;

public class ExchangeRateDTO {

    private String exchange;

    private BigDecimal rate;

    public ExchangeRateDTO() {
    }

    public ExchangeRateDTO(String exchange, BigDecimal rate) {
        this.exchange = exchange;
        this.rate = rate;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
