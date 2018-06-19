package com.rvs.challenge.mcc.currency.service;

import com.rvs.challenge.mcc.currency.dto.RoleDTO;
import com.rvs.challenge.mcc.currency.dto.UserDTO;
import com.rvs.challenge.mcc.currency.model.Role;
import com.rvs.challenge.mcc.currency.model.User;
import com.rvs.challenge.mcc.currency.repository.UserRepository;
import com.rvs.challenge.mcc.currency.util.ObjectParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * User implementation service.
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * Logger definition.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private SecurityService securityService;

    @Override
    public void save(UserDTO userData) {

        Set<Role> roles = new HashSet<>();

        if (userData.getRoles() != null) {

            for (RoleDTO roleData : userData.getRoles()) {
                roles.add(new Role(roleData.getId(), roleData.getName()));
            }
        }

        userRepository.save(new User(userData.getId(), userData.getUsername(), bCryptPasswordEncoder.encode(userData.getPassword()), roles));
    }

    @Override
    public Optional<UserDTO> findByUsername(String username) {

        LOGGER.info("findByUsername - {}", username);

        Optional<User> searchedUser = userRepository.findByUsername(username);

        LOGGER.info("findByUsername - {}", searchedUser.isPresent());

        UserDTO userData = null;

        if (searchedUser.isPresent()) {

            LOGGER.info("findByUsername - searchedUser.isPresent() {}", ObjectParserUtil.getInstance().toString(searchedUser.get()));

            Set<RoleDTO> rolesData = new HashSet<>();

            if (searchedUser.get().getRoles() != null) {
                for (Role userRole : searchedUser.get().getRoles()) {
                    rolesData.add(new RoleDTO(userRole.getId(), userRole.getName()));
                }
            }

            userData = new UserDTO(searchedUser.get().getId(), searchedUser.get().getUsername(), searchedUser.get().getPassword(), rolesData);
        }

        return Optional.ofNullable(userData);
    }

    @Override
    public Optional<UserDTO> findLoggedUser() {

        String username = securityService.findLoggedInUsername();

        LOGGER.info("findLoggedUser - {}", username);

        return this.findByUsername(username);

    }
}