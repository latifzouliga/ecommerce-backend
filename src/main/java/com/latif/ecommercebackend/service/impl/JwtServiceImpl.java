package com.latif.ecommercebackend.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.latif.ecommercebackend.model.User;
import com.latif.ecommercebackend.service.JwtService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

// Jwt is an encrypted string that is used as a credential identifies

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.algorithm.key}")
    private String algorithmKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.expiryInSeconds}")
    private int expiryInSecond;

    private Algorithm algorithm;

    private static final String USERNAME_KEY = "USERNAME";
    private static final String EMAIL_KEY = "email";

    @PostConstruct
    public void postConstruct(){
        algorithm = Algorithm.HMAC256(algorithmKey);
    }

    public String generateJWT(User user){
        return JWT.create()
                .withClaim(USERNAME_KEY,user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000L * expiryInSecond)))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    // extract username from token
    @Override
    public String getUsername(String token) {
        return JWT.decode(token).getClaim(USERNAME_KEY).asString();
    }

    @Override
    public String generateVerifiedJWT(User user) {
        return JWT.create()
                .withClaim(EMAIL_KEY,user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000L * expiryInSecond)))
                .withIssuer(issuer)
                .sign(algorithm);
    }
}
