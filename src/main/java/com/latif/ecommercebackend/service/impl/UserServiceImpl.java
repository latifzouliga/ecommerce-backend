package com.latif.ecommercebackend.service.impl;

import com.latif.ecommercebackend.dto.RegistrationBody;
import com.latif.ecommercebackend.exceptions.EcommerceProjectException;
import com.latif.ecommercebackend.model.LocalUser;

import com.latif.ecommercebackend.repository.LocalUserRepository;
import com.latif.ecommercebackend.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final LocalUserRepository userRepository;

    public UserServiceImpl(LocalUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public RegistrationBody registerUser(RegistrationBody registrationBody) throws EcommerceProjectException {

        if (userRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent() ||
                userRepository.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()
        ) {
            throw new EcommerceProjectException("User already exists");
        }

        LocalUser user = new LocalUser();
        user.setFirstName(registrationBody.getFirstName());
        user.setLastName(registrationBody.getLastName());
        user.setEmail(registrationBody.getEmail());
        user.setUsername(registrationBody.getUsername());
        user.setPassword(registrationBody.getPassword());
        // TODO: Encrypt password

        userRepository.save(user);
        return registrationBody;

    }
}
