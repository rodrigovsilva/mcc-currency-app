package com.rvs.challenge.mcc.currency.exception;

/**
 * Error on get rates conversion.
 */
public class ConversionRatesException extends RuntimeException {

    public ConversionRatesException(String exception) {
        super(exception);
    }

}
