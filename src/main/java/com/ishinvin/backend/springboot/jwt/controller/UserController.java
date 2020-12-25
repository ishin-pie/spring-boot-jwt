package com.ishinvin.backend.springboot.jwt.controller;

import com.ishinvin.backend.springboot.jwt.domain.User;
import com.ishinvin.backend.springboot.jwt.domain.pojo.UserRequest;
import com.ishinvin.backend.springboot.jwt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("v1/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.get(id);
    }

    @PostMapping("v1")
    public ResponseEntity<?> saveUser(@RequestBody UserRequest userRequest) {
        log.info("[USER CONTROLLER] save user");
        return ResponseEntity.ok(userService.save(userRequest));
    }

}
