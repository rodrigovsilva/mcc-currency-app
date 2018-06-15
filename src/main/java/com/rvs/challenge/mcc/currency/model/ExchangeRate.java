package com.rvs.challenge.mcc.currency.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Exchange Rates's model.
 */
@Entity
@Table(name = "ExchangeRate")
public class ExchangeRate {

    @Id
    @Basic(optional = false)
    @Column(unique=true, nullable = false)
    private String exchange;

    private BigDecimal rate;

    public ExchangeRate() {
    }

    public ExchangeRate(String exchange, BigDecimal rate) {
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
