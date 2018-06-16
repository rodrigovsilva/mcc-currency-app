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
    CurrencyConversionService currencyConverterService;

    /*@ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/convert")
    public ResponseEntity<Object> convert(@ModelAttribute("userForm") CurrencyConversionDTO currencyConversionForm, BindingResult bindingResult, Model model) {

        ResponseEntity<Object> responseEntity = null;

        try {

            responseEntity = new ResponseEntity<>(currencyConverterService.getConversionRates(currencyConversionForm), HttpStatus.OK);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            //responseEntity = new ResponseEntity<>(new ApiErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage(), e.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            responseEntity = new ResponseEntity<>("Ocorreu um erro ao acessar a operação.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;

    }*/

    @RequestMapping(value = {"/convert"}, method = RequestMethod.POST)
    public String convert(@ModelAttribute("conversionForm") CurrencyConversionDTO conversionForm, BindingResult bindingResult, Model model) {

        LOGGER.info("convert {} ", ObjectParserUtil.getInstance().toString(conversionForm));
        model.addAttribute("conversionForm", conversionForm);
        model.addAttribute("availableCurrencies", AvailableCurrencies.values());
        CurrencyConversionDTO conversionRates = currencyConverterService.getConversionRates(conversionForm);
        model.addAttribute("conversionRates", conversionRates);
        LOGGER.info("conversionRates on Controller {} ", ObjectParserUtil.getInstance().toString(conversionRates));

        return "main";
    }
}
