package com.rvs.challenge.mcc.currency.service;

import com.rvs.challenge.mcc.currency.dto.CurrencyConversionDTO;

import java.util.List;
import java.util.Set;

/**
 * Converter services interface.
 */
public interface CurrencyConversionService {

    /**
     * Get the conversion rate of a currency.
     *
     * @param conversion CurrencyConversion requested data.
     * @return CurrencyConversion with all available exchange rates.
     */
    CurrencyConversionDTO convert(CurrencyConversionDTO conversion);

    /**
     * Find all Currency Conversions.
     *
     * @param listSize Size of historical currency conversions.
     * @return List of currency conversions.
     */
    List<CurrencyConversionDTO> getHistoricalCurrencyConversions(int listSize);

    /**
     * Get all available currencies.
     *
     * @return List all available currencies.
     */
    Set<String> getAvailableCurrencies();
}
