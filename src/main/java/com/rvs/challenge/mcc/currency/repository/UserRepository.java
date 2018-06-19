package com.rvs.challenge.mcc.currency.repository;

import com.rvs.challenge.mcc.currency.model.User;
import org.springframework.data.repository.Repository;

import java.util.Optional;

/**
 * User repository.
 */
public interface UserRepository extends Repository<User, Long> {

    /**
     * Find user by username.
     *
     * @param username Username.
     * @return User found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Save user.
     *
     * @param user user to be saved.
     * @return user saved.
     */
    User save(User user);
}
