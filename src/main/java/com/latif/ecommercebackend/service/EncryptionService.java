package com.latif.ecommercebackend.service;

public interface EncryptionService {

    String encryptPassword(String password);

    public boolean verifyPassword(String password, String hash);
}
