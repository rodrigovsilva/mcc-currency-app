package com.rvs.challenge.mcc.currency.repository;

import com.rvs.challenge.mcc.currency.model.User;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long> {
    Optional<User> findByUsername(String username);
    User save(User user);
}
