package com.latif.ecommercebackend.service.impl;

import com.latif.ecommercebackend.dto.LoginBody;
import com.latif.ecommercebackend.dto.RegistrationBody;
import com.latif.ecommercebackend.exceptions.EcommerceProjectException;
import com.latif.ecommercebackend.mapper.MapperUtil;
import com.latif.ecommercebackend.model.LocalUser;

import com.latif.ecommercebackend.repository.LocalUserRepository;
import com.latif.ecommercebackend.service.EncryptionService;
import com.latif.ecommercebackend.service.JwtService;
import com.latif.ecommercebackend.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final LocalUserRepository userRepository;
    private final EncryptionService encryptionService;
    private final MapperUtil mapper;
    private final JwtService jwtService;

    public UserServiceImpl(LocalUserRepository userRepository,
                           EncryptionService encryptionService,
                           MapperUtil mapper,
                           JwtService jwtService) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.mapper = mapper;
        this.jwtService = jwtService;
    }


    @Override
    public RegistrationBody registerUser(RegistrationBody registrationBody) throws EcommerceProjectException {

        if (userRepository.findByEmailIgnoreCase(registrationBody.getEmail()).isPresent() ||
                userRepository.findByUsernameIgnoreCase(registrationBody.getUsername()).isPresent()
        ) {
            throw new EcommerceProjectException("User already exists");
        }

        LocalUser localUser = mapper.convert(registrationBody, LocalUser.class);

//        LocalUser user = new LocalUser();
//        user.setFirstName(registrationBody.getFirstName());
//        user.setLastName(registrationBody.getLastName());
//        user.setEmail(registrationBody.getEmail());
//        user.setUsername(registrationBody.getUsername());
//        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));

        localUser.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));

        userRepository.save(localUser);
        return registrationBody;

    }

    @Override
    public String LoginUser(LoginBody loginBody) throws EcommerceProjectException {

        // check if user exists in database
        Optional<LocalUser> opUser = userRepository.findByUsernameIgnoreCase(loginBody.getUsername());

        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            // verify login password is the same as the password in database
            if (encryptionService.verifyPassword(loginBody.getPassword(), user.getPassword())) {
                return jwtService.generateJWT(user);
            }
        }

        return null;
    }
}
