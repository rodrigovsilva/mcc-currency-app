package com.rvs.challenge.mcc.currency.web.validator;

import com.rvs.challenge.mcc.currency.dto.CurrencyConversionDTO;
import com.rvs.challenge.mcc.currency.dto.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Currency conversion validator.
 */
@Component
public class CurrencyConversionValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return CurrencyConversionDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        CurrencyConversionDTO conversion = (CurrencyConversionDTO) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "exchangeTo", "NotEmpty.currencyConversionForm.exchange");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "exchangeFrom", "NotEmpty.currencyConversionForm.exchange");

        if (conversion.getTimestamp() != null) {
            errors.rejectValue("timestamp", "NotEmpty.currencyConversionForm.timestamp");
        }

    }
}
