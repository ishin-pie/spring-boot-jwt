package com.ishinvin.backend.springboot.jwt.domain.pojo;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest {
    private String username;
    private String password;
}
