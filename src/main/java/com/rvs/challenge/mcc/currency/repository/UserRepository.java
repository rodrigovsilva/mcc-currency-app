package com.rvs.challenge.mcc.currency.repository;

import com.rvs.challenge.mcc.currency.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
