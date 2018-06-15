package com.rvs.challenge.mcc.currency.service;

import com.rvs.challenge.mcc.currency.dto.RoleDTO;
import com.rvs.challenge.mcc.currency.dto.UserDTO;
import com.rvs.challenge.mcc.currency.model.Role;
import com.rvs.challenge.mcc.currency.model.User;
import com.rvs.challenge.mcc.currency.repository.RoleRepository;
import com.rvs.challenge.mcc.currency.repository.UserRepository;
import com.rvs.challenge.mcc.currency.util.ObjectParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    /**
     * Logger definition.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(UserDTO userData) {

        Set<Role> roles = new HashSet<>();

        LOGGER.info("userData\n" + ObjectParserUtil.getInstance().toString(userData));

        if(userData.getRoles() != null) {

            for (RoleDTO roleData : userData.getRoles()) {
                roles.add(new Role(roleData.getId(), roleData.getName()));
            }
        }

        User user = new User(userData.getId(), userData.getUsername(), bCryptPasswordEncoder.encode(userData.getPassword()), roles);

        LOGGER.info("user\n" + ObjectParserUtil.getInstance().toString(user));

        userRepository.save(user);
    }

    @Override
    public UserDTO findByUsername(String username) {

        LOGGER.info("username " + username);

        User searchedUser = userRepository.findByUsername(username);
        UserDTO userData = null;

        if(searchedUser != null) {
            LOGGER.info("searchedUser\n" + ObjectParserUtil.getInstance().toString(searchedUser));

            Set<RoleDTO> rolesData = new HashSet<>();

            if(searchedUser.getRoles() != null) {
                for (Role userRole : searchedUser.getRoles()) {
                    rolesData.add(new RoleDTO(userRole.getId(), userRole.getName()));
                }
            }

            userData = new UserDTO(searchedUser.getId(), searchedUser.getUsername(), searchedUser.getPassword(), rolesData);

            LOGGER.info("userData\n" + ObjectParserUtil.getInstance().toString(userData));
        }

        return userData;
    }
}
