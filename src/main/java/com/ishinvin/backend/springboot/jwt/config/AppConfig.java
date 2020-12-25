package com.ishinvin.backend.springboot.jwt.config;

import com.ishinvin.backend.springboot.jwt.domain.Role;
import com.ishinvin.backend.springboot.jwt.domain.User;
import com.ishinvin.backend.springboot.jwt.domain.enums.Roles;
import com.ishinvin.backend.springboot.jwt.exception.InvalidArgumentsException;
import com.ishinvin.backend.springboot.jwt.exception.ObjectNotFoundException;
import com.ishinvin.backend.springboot.jwt.repository.RoleRepository;
import com.ishinvin.backend.springboot.jwt.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
public class AppConfig {

    // init app user
    private static final String APP_ADMIN_USERNAME = "admin@example.com";
    private static final String APP_ADMIN_PASSWORD = "admin";
    private static final String APP_USER_USERNAME = "test@example.com";
    private static final String APP_USER_PASSWORD = "test";


    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void initUser() {
        log.info("[INITIALIZE ROLE] initialize role");
        Optional<Role> roleAdmin = roleRepository.findByName(Roles.ROLE_ADMIN);
        if(!roleAdmin.isPresent()) {
            Role role = new Role();
            role.setName(Roles.ROLE_ADMIN);
            log.info("[INITIALIZE ROLE] role admin save {}", roleRepository.save(role));
        }

        Optional<Role> roleUser = roleRepository.findByName(Roles.ROLE_USER);
        if(!roleUser.isPresent()) {
            Role role = new Role();
            role.setName(Roles.ROLE_USER);
            log.info("[INITIALIZE ROLE] role user save {}", roleRepository.save(role));
        }

        log.info("[INITIALIZE USER] initialize user");
        Optional<User> admin = repository.findByUsername(APP_ADMIN_USERNAME);
        if(!admin.isPresent()) {
            User initAdmin = new User();
            initAdmin.setUsername(APP_ADMIN_USERNAME);
            initAdmin.setPassword(passwordEncoder.encode(APP_ADMIN_PASSWORD));
            // set user role
            List<Role> userRoles = roleRepository.findAll();
            if(userRoles.isEmpty()) {
                throw new InvalidArgumentsException("Invalid user roles");
            }
            initAdmin.setRoles(userRoles);

            initAdmin.setIsActive(true);
            initAdmin.setIsDeleted(false);
            Date currentDate = new Date();
            initAdmin.setCreatedDateTime(currentDate);
            initAdmin.setModifiedDateTime(currentDate);
            log.info("[INITIALIZE USER] admin save {}", repository.save(initAdmin));
        }

        Optional<User> user = repository.findByUsername(APP_USER_USERNAME);
        if(!user.isPresent()) {
            User initUser = new User();
            initUser.setUsername(APP_USER_USERNAME);
            initUser.setPassword(passwordEncoder.encode(APP_USER_PASSWORD));
            // set user role
            Role role = roleRepository.findByName(Roles.ROLE_USER).orElseThrow(() -> new ObjectNotFoundException("Invalid user role: " + Roles.ROLE_USER.name()));
            List<Role> userRoles = new ArrayList<>();
            userRoles.add(role);
            initUser.setRoles(userRoles);
            initUser.setIsActive(true);
            initUser.setIsDeleted(false);
            Date currentDate = new Date();
            initUser.setCreatedDateTime(currentDate);
            initUser.setModifiedDateTime(currentDate);
            log.info("[INITIALIZE USER] user save {}", repository.save(initUser));
        }
    }

}
