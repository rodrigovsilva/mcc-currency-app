package com.rvs.challenge.mcc.currency.web;

import com.rvs.challenge.mcc.currency.dto.UserDTO;
import com.rvs.challenge.mcc.currency.service.CurrencyConversionService;
import com.rvs.challenge.mcc.currency.service.SecurityService;
import com.rvs.challenge.mcc.currency.service.UserService;
import com.rvs.challenge.mcc.currency.web.validator.UserRegistrationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.invoke.MethodHandles;
import java.util.Locale;

/**
 * User controller.
 */
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
    private UserRegistrationValidator userRegistrationValidator;

    @Autowired
    private MessageSource messageSource;

    /**
     * Open User registration screen.
     *
     * @param model data model
     * @return Router
     */
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new UserDTO());

        return "registration";
    }

    /**
     * Register a user.
     *
     * @param userForm      User form.
     * @param bindingResult binding result.
     * @param model         data model.
     * @return Router.
     */
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@ModelAttribute("userForm") UserDTO userForm, BindingResult bindingResult, Model model) {
        try {
            userRegistrationValidator.validate(userForm, bindingResult);

            if (bindingResult.hasErrors()) {
                return "registration";
            }

            userService.save(userForm);

            securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());
        } catch (UsernameNotFoundException e) {
            model.addAttribute("message", "Please, log in to convert currencies.");
            return "login";

        } catch (Exception e) {
            model.addAttribute("error", "Something is going wrong. Please, try later.");

        }
        return "redirect:/convert";
    }

    /**
     * Open User login screen.
     *
     * @param model  Data model
     * @param error  errors.
     * @param logout logout parameter.
     * @return Router.
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        try {
            if (error != null)
                model.addAttribute("error", messageSource.getMessage("Credentials.loginForm.invalid", null, Locale.getDefault()));

            if (logout != null)
                model.addAttribute("message", messageSource.getMessage("Logout.success", null, Locale.getDefault()));
        } catch (UsernameNotFoundException e) {
            model.addAttribute("message", "Please, log in to convert currencies.");
            return "login";

        } catch (Exception e) {
            model.addAttribute("error", "Something is going wrong. Please, try later.");

        }
        return "login";
    }

}
