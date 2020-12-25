package com.ishinvin.backend.springboot.jwt.domain.pojo;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
}
