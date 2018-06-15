package com.rvs.challenge.mcc.currency.model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

/**
 * CurrencyConversion model.
 */
@Entity
@Table(name = "CurrencyConversion")
public class CurrencyConversion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id",unique=true, nullable = false)
    private Long id;

    private String exchange;

    private Calendar timestamp;

    @OneToMany(cascade = CascadeType.ALL)
    private List<ExchangeRate> exchangeRates;

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

    public List<ExchangeRate> getExchangeRates() {
        return exchangeRates;
    }

    public void setExchangeRates(List<ExchangeRate> exchangeRates) {
        this.exchangeRates = exchangeRates;
    }
}

