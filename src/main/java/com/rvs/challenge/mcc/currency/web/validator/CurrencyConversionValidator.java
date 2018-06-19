package com.rvs.challenge.mcc.currency.web.validator;

import com.rvs.challenge.mcc.currency.dto.CurrencyConversionDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.lang.invoke.MethodHandles;

/**
 * Currency conversion validator.
 */
@Component
public class CurrencyConversionValidator implements Validator {
    /**
     * Logger definition.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public boolean supports(Class<?> aClass) {
        return CurrencyConversionDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        CurrencyConversionDTO conversion = (CurrencyConversionDTO) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "exchangeTo", "NotEmpty.currencyConversionForm.exchange");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "exchangeFrom", "NotEmpty.currencyConversionForm.exchange");

        if (StringUtils.isNotEmpty(conversion.getExchangeTo()) && StringUtils.isNotEmpty(conversion.getExchangeFrom()) &&
                StringUtils.equalsIgnoreCase(conversion.getExchangeTo(), conversion.getExchangeFrom())) {
            errors.rejectValue("exchangeFrom", "EqualsNotPermitted.currencyConversionForm.invalid");
        }

        LOGGER.info("validate {}", conversion.getTimestamp());
        if (conversion.getTimestamp() == null) {
            errors.rejectValue("timestamp", "NotEmpty.currencyConversionForm.timestamp");
        }

    }
}
