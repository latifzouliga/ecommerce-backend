package com.latif.ecommercebackend.service;

import com.latif.ecommercebackend.model.LocalUser;

public interface JwtService {

    String generateJWT(LocalUser user);
}
