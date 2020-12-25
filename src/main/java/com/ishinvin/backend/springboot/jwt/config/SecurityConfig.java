package com.ishinvin.backend.springboot.jwt.config;

import com.ishinvin.backend.springboot.jwt.domain.enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("appUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // configure AuthenticationManager so that it knows from where to load
        // user for matching credentials
        // Use BCryptPasswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic().disable()
                .csrf().disable()

                // add a filter to validate the tokens with every request
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/api/auth/token").permitAll()
                .antMatchers("/api/auth/register").permitAll()
                //.antMatchers("/api-docs/**").hasAuthority(Roles.ROLE_ADMIN.name())
                .antMatchers("/api/users/**").hasAuthority(Roles.ROLE_ADMIN.name())
                .antMatchers("/api/**").authenticated()
                .and()

                // custom exception handling
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()

                //allow use of frame to same origin urls (for h2-console)
                .headers().frameOptions().sameOrigin()
                .and()

                // make sure we use stateless session; session won't be used to store user's state.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
