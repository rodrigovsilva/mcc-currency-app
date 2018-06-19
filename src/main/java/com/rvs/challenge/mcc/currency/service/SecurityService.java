package com.rvs.challenge.mcc.currency.service;

/**
 * Security interface.
 */
public interface SecurityService {

    /**
     * Find logged username.
     * @return
     */
    String findLoggedInUsername();

    /**
     * Auto login method.
     * @param username username.
     * @param password password.
     */
    void autologin(String username, String password);
}
