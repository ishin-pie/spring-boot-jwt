package com.ishinvin.backend.springboot.jwt.service.implement;

import com.ishinvin.backend.springboot.jwt.domain.Role;
import com.ishinvin.backend.springboot.jwt.domain.User;
import com.ishinvin.backend.springboot.jwt.domain.enums.Roles;
import com.ishinvin.backend.springboot.jwt.domain.pojo.UserRequest;
import com.ishinvin.backend.springboot.jwt.exception.InvalidArgumentsException;
import com.ishinvin.backend.springboot.jwt.exception.ObjectNotFoundException;
import com.ishinvin.backend.springboot.jwt.repository.RoleRepository;
import com.ishinvin.backend.springboot.jwt.repository.UserRepository;
import com.ishinvin.backend.springboot.jwt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImplement implements UserService {

    private static final String LOG = "[USER SERVICE] {}";

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User get(Long id) {
        log.info(LOG + " - ID: {}", "getUserById", id);
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User not found: " + id));
    }

    @Override
    @Transactional
    public User save(UserRequest userRequest) {
        // check user data
        checkForValidUserData(userRequest);

        //if(userRequest.getRole() == null) {
        //    log.info(LOG, "failed - user role is required");
        //    throw new InvalidArgumentsException("User Role is required");
        //}

        // set user data
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        // set user role
        List<Role> userRoles = roleRepository.findAll();
        if(userRoles.isEmpty()) {
            throw new InvalidArgumentsException("Invalid user roles");
        }
        user.setRoles(userRoles);

        user.setIsActive(true);
        user.setIsDeleted(false);

        // current date
        Date currentDate = new Date();
        user.setCreatedDateTime(currentDate);
        user.setModifiedDateTime(currentDate);

        log.info(LOG + " - {}", "save user", user);

        return repository.save(user);
    }

    @Override
    @Transactional
    public User register(UserRequest userRequest) {
        //check user data
        checkForValidUserData(userRequest);

        // set user data
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        // set user role
        Role role = roleRepository.findByName(Roles.ROLE_USER).orElseThrow(() -> new ObjectNotFoundException("Invalid user role: " + Roles.ROLE_USER.name()));
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(role);
        user.setRoles(userRoles);

        // todo: should set to false and verify email
        user.setIsActive(true);
        user.setIsDeleted(false);

        // current date
        Date currentDate = new Date();
        user.setCreatedDateTime(currentDate);
        user.setModifiedDateTime(currentDate);

        log.info(LOG + " - {}", "user registration", user);

        return repository.save(user);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        log.info(LOG, "findAll");
        return repository.findAll(pageable);
    }

    @Override
    public User findByUsername(String username) {
        log.info(LOG + " - username: {}", "findByUsername", username);
        return repository.findByUsername(username).orElseThrow(() -> new ObjectNotFoundException("User not found: " + username));
    }

    @Override
    public User findByUsernameAndIsActiveTrue(String username) {
        return repository.findByUsernameAndIsActiveTrue(username).orElseThrow(() -> new ObjectNotFoundException("User not found: " + username));
    }

    private void checkForValidUserData(UserRequest userRequest) {

        if(!StringUtils.hasText(userRequest.getUsername())) {
            log.info(LOG, "failed - username is required");
            throw new InvalidArgumentsException("Username is required");
        }

        if(!StringUtils.hasText(userRequest.getPassword())) {
            log.info(LOG, "failed - password is required");
            throw new InvalidArgumentsException("Password is required");
        }

        // todo: check for valid password

        Optional<User> findUser = repository.findByUsername(userRequest.getUsername());

        if(findUser.isPresent()) {
            log.info(LOG, "failed - username is already existed");
            throw new InvalidArgumentsException("Username is already existed");
        }

    }

}
