package com.rvs.challenge.mcc.currency.web;

import com.rvs.challenge.mcc.currency.domain.AvailableCurrencies;
import com.rvs.challenge.mcc.currency.dto.CurrencyConversionDTO;
import com.rvs.challenge.mcc.currency.dto.UserDTO;
import com.rvs.challenge.mcc.currency.service.CurrencyConversionService;
import com.rvs.challenge.mcc.currency.util.ObjectParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Controller
public class CurrencyController {

    /**
     * Logger definition.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    CurrencyConversionService currencyConversionService;


    @RequestMapping(value = {"/convert"}, method = RequestMethod.POST)
    public String convert(@ModelAttribute("conversionForm") CurrencyConversionDTO conversionForm, BindingResult bindingResult, Model model) {

        try {
            CurrencyConversionDTO conversionRate = currencyConversionService.convert(conversionForm);
            List<CurrencyConversionDTO> historicalConversions = currencyConversionService.getHistoricalCurrencyConversions(10);

            LOGGER.info("convert {} ", ObjectParserUtil.getInstance().toString(conversionForm));

            model.addAttribute("conversionForm", new CurrencyConversionDTO());
            model.addAttribute("availableCurrencies", AvailableCurrencies.values());
            model.addAttribute("conversionRate", conversionRate);
            model.addAttribute("historicalConversions", historicalConversions);

            LOGGER.info("historicalConversions on Controller {} ", ObjectParserUtil.getInstance().toString(historicalConversions));

            LOGGER.info("conversionRates on Controller {} ", ObjectParserUtil.getInstance().toString(conversionRate));

        }catch (UsernameNotFoundException e) {
            model.addAttribute("message", "Please, log in to convert currencies.");

            return "login";

        } catch (Exception e) {
            model.addAttribute("error", "Something is going wrong. Please, try later.");

        }
        return "main";
    }
}
