package com.rvs.challenge.mcc.currency.repository;

import com.rvs.challenge.mcc.currency.model.CurrencyConversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface CurrencyConversionRepository extends Repository<CurrencyConversion, Long> {

    Optional<Page<CurrencyConversion>> findAll(Pageable page);

    void save(CurrencyConversion currencyConversion);

}
