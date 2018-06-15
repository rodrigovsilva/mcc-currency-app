package com.rvs.challenge.mcc.currency.service;

public interface SecurityService {
    String findLoggedInUsername();

    void autologin(String username, String password);
}
