package com.rvs.challenge.mcc.currency.web;

import com.rvs.challenge.mcc.currency.domain.AvailableCurrencies;
import com.rvs.challenge.mcc.currency.dto.CurrencyConversionDTO;
import com.rvs.challenge.mcc.currency.dto.UserDTO;
import com.rvs.challenge.mcc.currency.service.CurrencyConversionService;
import com.rvs.challenge.mcc.currency.util.ObjectParserUtil;
import com.rvs.challenge.mcc.currency.web.property.editor.CustomCalendarEditor;
import com.rvs.challenge.mcc.currency.web.validator.CurrencyConversionValidator;
import com.rvs.challenge.mcc.currency.web.validator.UserRegistrationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.Calendar;
import java.util.List;

/**
 * Currency conversion controller.
 */
@Controller
public class CurrencyController {

    /**
     * Logger definition.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    CurrencyConversionService currencyConversionService;

    @Autowired
    private CurrencyConversionValidator currencyConversionValidator;

    /**
     * Convert the selected exchanges.
     *
     * @param conversionForm conversion form data.
     * @param bindingResult  binding result
     * @param model          data model
     * @return Router.
     */
    @RequestMapping(value = {"/convert"}, method = RequestMethod.POST)
    public String convert(@ModelAttribute("conversionFormData") CurrencyConversionDTO conversionForm, BindingResult bindingResult, Model model) {

        try {
            List<CurrencyConversionDTO> historicalConversions = currencyConversionService.getHistoricalCurrencyConversions(10);

            model.addAttribute("availableCurrencies", AvailableCurrencies.values());
            model.addAttribute("historicalConversions", historicalConversions);

            currencyConversionValidator.validate(conversionForm, bindingResult);

            if (bindingResult.hasErrors()) {
                return "main";
            }

            LOGGER.info("convert {} ", ObjectParserUtil.getInstance().toString(conversionForm));

            CurrencyConversionDTO conversionRate = currencyConversionService.convert(conversionForm);
            CurrencyConversionDTO newConversionFormData = new CurrencyConversionDTO();
            newConversionFormData.setTimestamp(Calendar.getInstance());

            model.addAttribute("conversionFormData", newConversionFormData);
            model.addAttribute("conversionRate", conversionRate);

            LOGGER.info("historicalConversions on Controller {} ", ObjectParserUtil.getInstance().toString(historicalConversions));

            LOGGER.info("conversionRates on Controller {} ", ObjectParserUtil.getInstance().toString(conversionRate));

        } catch (UsernameNotFoundException e) {
            model.addAttribute("message", "Please, log in to convert currencies.");
            return "login";

        } catch (Exception e) {
            model.addAttribute("error", "Something is going wrong. Please, try later.");

        }
        return "main";
    }

    /**
     * Binder for calendar data.
     *
     * @param webDataBinder web binder.
     */
    @InitBinder("conversionFormData")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(Calendar.class, new CustomCalendarEditor());
    }
}
