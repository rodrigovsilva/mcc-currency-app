package com.rvs.challenge.mcc.currency.service;

import com.rvs.challenge.mcc.currency.dto.UserDTO;

public interface UserService {
    void save(UserDTO user);

    UserDTO findByUsername(String username);
}
