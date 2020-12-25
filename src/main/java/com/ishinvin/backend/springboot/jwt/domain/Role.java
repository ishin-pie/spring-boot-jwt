package com.ishinvin.backend.springboot.jwt.domain;

import com.ishinvin.backend.springboot.jwt.domain.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private Roles name;

    @Version
    @Column(name = "version")
    private Integer version;

}
