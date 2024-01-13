package com.latif.ecommercebackend.repository;

import com.latif.ecommercebackend.model.VerificationToken;
import org.springframework.data.repository.ListCrudRepository;

public interface VerificationTokenRepository extends ListCrudRepository<VerificationToken, Long> {
}
