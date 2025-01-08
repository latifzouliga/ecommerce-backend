package com.latif.ecommercebackend.service;

import com.latif.ecommercebackend.model.User;

public interface JwtService {

    String generateJWT(User user);
    String getUsername(String token);

    String generateVerifiedJWT(User user);
}
