package com.ishinvin.backend.springboot.jwt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "users")
@Where(clause = "is_deleted = false")
public class User extends BaseDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Email
    @NotEmpty
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @JsonIgnore
    @ToString.Exclude
    @NotEmpty
    @Column(name = "password", nullable = false)
    private String password;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

}
