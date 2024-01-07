package com.latif.ecommercebackend.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.latif.ecommercebackend.model.LocalUser;
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

    private static final String USERNAME_KEY= "USERNAME";

    @PostConstruct
    public void postConstruct(){
        algorithm = Algorithm.HMAC256(algorithmKey);
    }

    public String generateJWT(LocalUser user){
        return JWT.create()
                .withClaim(USERNAME_KEY,user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (1000L * expiryInSecond)))
                .withIssuer(issuer)
                .sign(algorithm);
    }
}