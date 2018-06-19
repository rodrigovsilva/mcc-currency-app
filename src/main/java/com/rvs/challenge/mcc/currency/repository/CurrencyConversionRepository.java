package com.rvs.challenge.mcc.currency.repository;

import com.rvs.challenge.mcc.currency.model.CurrencyConversion;
import com.rvs.challenge.mcc.currency.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * Currency conversion repository.
 */
public interface CurrencyConversionRepository extends Repository<CurrencyConversion, Long> {

    /**
     * Find all currency conversions by user in pages.
     *
     * @param user user of conversions.
     * @param page pageable object.
     * @return Pageable conversions list.
     */
    Optional<Page<CurrencyConversion>> findAllByUser(User user, Pageable page);

    /**
     * Save a conversion.
     *
     * @param currencyConversion conversion to be saved.
     */
    void save(CurrencyConversion currencyConversion);

}
