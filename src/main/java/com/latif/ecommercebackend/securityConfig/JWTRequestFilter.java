package com.latif.ecommercebackend.securityConfig;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.latif.ecommercebackend.model.LocalUser;
import com.latif.ecommercebackend.repository.LocalUserRepository;
import com.latif.ecommercebackend.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final LocalUserRepository userRepository;

    public JWTRequestFilter(JwtService jwtService, LocalUserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    // very time there is a request, it will come to this method
    //  first we need to check, does it have the header "Authorization", does it start with Bearer keyword
    // then if it does, we need to break the substring the Bearer token and then decode the token and the username
    // and then we check if that username is available in repository
    // if it is, then we need to build the authentication object of that user
    // set details so that spring security and spring MVC knows about it


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);
            try {
                String username = jwtService.getUsername(token);
                Optional<LocalUser> opUser = userRepository.findByUsernameIgnoreCase(username);
                if (opUser.isPresent()) {
                    LocalUser user = opUser.get();
                    // build authentication object
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());

                    // set details so that spring security and spring MVC knows about it
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // where the authentication is stored, we get the context and set the authentication
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (JWTDecodeException exception) {

            }
        }
        filterChain.doFilter(request, response);
    }
}
