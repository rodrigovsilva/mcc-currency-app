package com.rvs.challenge.mcc.currency.service;

import com.rvs.challenge.mcc.currency.dto.UserDTO;

import java.util.Optional;

public interface UserService {

    void save(UserDTO user);

    Optional<UserDTO> findByUsername(String username);

    Optional<UserDTO> findLoggedUser();
}
