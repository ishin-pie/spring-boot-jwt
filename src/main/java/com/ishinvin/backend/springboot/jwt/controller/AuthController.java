package com.ishinvin.backend.springboot.jwt.controller;

import com.ishinvin.backend.springboot.jwt.config.JwtTokenUtil;
import com.ishinvin.backend.springboot.jwt.domain.User;
import com.ishinvin.backend.springboot.jwt.domain.pojo.JwtRequest;
import com.ishinvin.backend.springboot.jwt.domain.pojo.JwtResponse;
import com.ishinvin.backend.springboot.jwt.domain.pojo.UserRequest;
import com.ishinvin.backend.springboot.jwt.exception.InvalidArgumentsException;
import com.ishinvin.backend.springboot.jwt.exception.InvalidCredentialException;
import com.ishinvin.backend.springboot.jwt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier("appUserDetailsService")
    private UserDetailsService userDetailsService;

    @GetMapping("me")
    public User getMe() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("[GET CURRENT USER] getMe - {}", userDetails);
        return userService.findByUsername(userDetails.getUsername());
    }

    @PostMapping("token")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest jwtRequest) {

        if(!StringUtils.hasText(jwtRequest.getUsername())) {
            throw new InvalidArgumentsException("username is required.");
        }

        if(!StringUtils.hasText(jwtRequest.getPassword())) {
            throw new InvalidArgumentsException("password is required.");
        }

        log.info("[AUTH CONTROLLER] createAuthenticationToken - username: {}", jwtRequest.getUsername());

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        } catch (Exception e) {
            throw new InvalidCredentialException("username or password is invalid.");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody UserRequest userRequest) {
        log.info("[AUTH CONTROLLER] register");
        return ResponseEntity.ok(userService.register(userRequest));
    }

}
