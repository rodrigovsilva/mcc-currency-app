package com.rvs.challenge.mcc.currency.web;

import com.rvs.challenge.mcc.currency.domain.AvailableCurrencies;
import com.rvs.challenge.mcc.currency.dto.UserDTO;
import com.rvs.challenge.mcc.currency.service.CurrencyConversionService;
import com.rvs.challenge.mcc.currency.service.SecurityService;
import com.rvs.challenge.mcc.currency.service.UserService;
import com.rvs.challenge.mcc.currency.util.ObjectParserUtil;
import com.rvs.challenge.mcc.currency.web.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.invoke.MethodHandles;

@Controller
public class UserController {

    /**
     * Logger definition.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Autowired
    private UserService userService;

    @Autowired
    private CurrencyConversionService currencyConversionService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new UserDTO());

        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") UserDTO userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/main";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @RequestMapping(value = {"/", "/main"}, method = RequestMethod.GET)
    public String main(Model model) {

        model.addAttribute("availableCurrencies", AvailableCurrencies.values());
        model.addAttribute("historicalConversions", currencyConversionService.getHistoricalCurrencyConversions(10));

        LOGGER.info("main {} ", ObjectParserUtil.getInstance().toString(AvailableCurrencies.values()));
        return "main";
    }
}
