package com.rvs.challenge.mcc.currency.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * CurrencyConversion model.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
        allowGetters = true)

@Table(name = "currencyConversion")
public class CurrencyConversion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "conversion_id", unique = true, nullable = false)
    private Long id;

    private String exchangeFrom;

    private String exchangeTo;

    private Calendar timestamp;

    private BigDecimal rate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Calendar createdAt;

    public CurrencyConversion(Long id, String exchangeFrom, String exchangeTo, Calendar timestamp, BigDecimal rate, User user) {
        this.id = id;
        this.exchangeFrom = exchangeFrom;
        this.exchangeTo = exchangeTo;
        this.timestamp = timestamp;
        this.rate = rate;
        this.user = user;
    }

    public CurrencyConversion(String exchangeFrom, String exchangeTo, Calendar timestamp, BigDecimal rate, User user) {
        this.exchangeFrom = exchangeFrom;
        this.exchangeTo = exchangeTo;
        this.timestamp = timestamp;
        this.rate = rate;
        this.user = user;
    }

    public CurrencyConversion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

