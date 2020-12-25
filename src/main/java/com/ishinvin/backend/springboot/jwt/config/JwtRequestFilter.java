package com.ishinvin.backend.springboot.jwt.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_HEADER = "Bearer ";

    @Autowired
    @Qualifier("appUserDetailsService")
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/api/")) {

            final String requestTokenHeader = request.getHeader(AUTHORIZATION_HEADER);

            String username = null;
            String jwtToken = null;

            // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
            if (requestTokenHeader != null && requestTokenHeader.startsWith(BEARER_HEADER)) {
                jwtToken = requestTokenHeader.substring(7);
                try {
                    username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                } catch (
                        IllegalArgumentException |
                        ExpiredJwtException |
                        SignatureException |
                        MalformedJwtException |
                        UnsupportedJwtException e
                ) {
                    log.warn(e.getMessage());
                }
            }

            //Once we get the token validate it.
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // get user
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // if token is valid configure Spring Security to manually set authentication
                if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // After setting the Authentication in the context, we specify that the current user is authenticated.
                    // So it passes the Spring Security Configurations successfully.
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

        }

        filterChain.doFilter(request, response);

    }

}
