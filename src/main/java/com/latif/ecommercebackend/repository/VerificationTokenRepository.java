package com.latif.ecommercebackend.repository;

import com.latif.ecommercebackend.model.User;
import com.latif.ecommercebackend.model.VerificationToken;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends ListCrudRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);
    void deleteByUser(User user);
}
