package com.ishinvin.backend.springboot.jwt.service;

import com.ishinvin.backend.springboot.jwt.domain.User;
import com.ishinvin.backend.springboot.jwt.domain.pojo.UserRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User get(Long id);

    User save(UserRequest userRequest);

    User register(UserRequest userRequest);

    Page<User> findAll(Pageable pageable);

    User findByUsername(String username);

    User findByUsernameAndIsActiveTrue(String username);

}
