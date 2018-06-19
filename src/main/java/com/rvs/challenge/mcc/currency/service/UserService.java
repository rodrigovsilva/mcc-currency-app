package com.rvs.challenge.mcc.currency.service;

import com.rvs.challenge.mcc.currency.dto.UserDTO;

import java.util.Optional;

/**
 * User Service.
 */
public interface UserService {

    /**
     * Save user.
     * @param user User to be saved.
     */
    void save(UserDTO user);

    /**
     * Find user by username
     * @param username username.
     * @return User found.
     */
    Optional<UserDTO> findByUsername(String username);

    /**
     * Find logger user.
     * @return User logged in.
     */
    Optional<UserDTO> findLoggedUser();
}
