package com.rvs.challenge.mcc.currency.web;

import com.rvs.challenge.mcc.currency.dto.CurrencyConversionDTO;
import com.rvs.challenge.mcc.currency.exception.ConversionRatesException;
import com.rvs.challenge.mcc.currency.service.CurrencyConversionService;
import com.rvs.challenge.mcc.currency.util.ObjectParserUtil;
import com.rvs.challenge.mcc.currency.web.validator.CurrencyConversionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Currency conversion controller.
 */
@Controller
public class CurrencyController {

    /**
     * Logger definition.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Date format.
     */
    private static final SimpleDateFormat SDF = new SimpleDateFormat("MM/dd/yyyy");

    @Autowired
    CurrencyConversionService currencyConversionService;

    @Autowired
    private CurrencyConversionValidator currencyConversionValidator;

    @Autowired
    private MessageSource messageSource;

    /**
     * Open main screen.
     *
     * @param model data model.
     * @return Router.
     */
    @RequestMapping(value = {"/", "/convert"}, method = RequestMethod.GET)
    public String convert(Model model) {

        try {

            initConversionLists(model);
            initConversionForm(model);

        } catch (Exception e) {
            model.addAttribute("error", messageSource.getMessage("Global.error", null, Locale.getDefault()));
            return "login";
        }
        return "convert";
    }

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

            currencyConversionValidator.validate(conversionForm, bindingResult);

            if (bindingResult.hasErrors()) {
                return "convert";
            }

            LOGGER.info("convert {} ", ObjectParserUtil.getInstance().toString(conversionForm));

            CurrencyConversionDTO conversionRate = currencyConversionService.convert(conversionForm);
            model.addAttribute("conversionRate", conversionRate);

            initConversionForm(model);

            LOGGER.info("conversionRates on Controller {} ", ObjectParserUtil.getInstance().toString(conversionRate));

        } catch (ConversionRatesException | UsernameNotFoundException e) {
            model.addAttribute("message", e.getMessage());
            return "login";

        } catch (Exception e) {
            model.addAttribute("error", messageSource.getMessage("Global.error", null, Locale.getDefault()));

        } finally {
            initConversionLists(model);
        }
        return "convert";
    }

    /**
     * Init conversion data model.
     *
     * @param model Data model.
     */
    private void initConversionLists(Model model) {

        List<CurrencyConversionDTO> historicalConversions = currencyConversionService.getHistoricalCurrencyConversions(10);
        Set<String> availableCurrencies = currencyConversionService.getAvailableCurrencies();

        model.addAttribute("availableCurrencies", availableCurrencies);
        model.addAttribute("historicalConversions", historicalConversions);

        LOGGER.info("availableCurrencies {}", ObjectParserUtil.getInstance().toString(availableCurrencies));
        LOGGER.info("historicalConversions on Controller {} ", ObjectParserUtil.getInstance().toString(historicalConversions));

    }

    /**
     * Init conversion data model.
     *
     * @param model Data model.
     */
    private void initConversionForm(Model model) {
        CurrencyConversionDTO currencyConversionForm = new CurrencyConversionDTO();
        currencyConversionForm.setTimestamp(Calendar.getInstance().getTime());
        model.addAttribute("conversionFormData", currencyConversionForm);

    }

    /**
     * Binder for calendar data.
     *
     * @param webDataBinder web binder.
     */
    @InitBinder("conversionFormData")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(SDF, true));
    }
}
