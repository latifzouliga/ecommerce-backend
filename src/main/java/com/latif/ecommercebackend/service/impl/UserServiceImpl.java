package com.latif.ecommercebackend.service.impl;

import com.latif.ecommercebackend.dto.LoginBody;
import com.latif.ecommercebackend.dto.RegistrationBody;
import com.latif.ecommercebackend.exceptions.EcommerceProjectException;
import com.latif.ecommercebackend.exceptions.EmailFailureException;
import com.latif.ecommercebackend.mapper.MapperUtil;
import com.latif.ecommercebackend.model.LocalUser;

import com.latif.ecommercebackend.model.VerificationToken;
import com.latif.ecommercebackend.repository.LocalUserRepository;
import com.latif.ecommercebackend.repository.VerificationTokenRepository;
import com.latif.ecommercebackend.service.EncryptionService;
import com.latif.ecommercebackend.service.JwtService;
import com.latif.ecommercebackend.service.UserService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final LocalUserRepository userRepository;
    private final EncryptionService encryptionService;
    private final MapperUtil mapper;
    private final JwtService jwtService;
    private final EmailServiceImpl emailService;
    private final VerificationTokenRepository verificationTokenRepository;

    public UserServiceImpl(LocalUserRepository userRepository,
                           EncryptionService encryptionService,
                           MapperUtil mapper,
                           JwtService jwtService,
                           EmailServiceImpl emailService,
                           VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.mapper = mapper;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.verificationTokenRepository = verificationTokenRepository;
    }


    @Override
    public RegistrationBody registerUser(RegistrationBody registrationBody) throws EcommerceProjectException, EmailFailureException {

        if (userRepository.findByEmailIgnoreCase(registrationBody.email()).isPresent() ||
                userRepository.findByUsernameIgnoreCase(registrationBody.username()).isPresent()
        ) {
            throw new EcommerceProjectException("User already exists");
        }

        LocalUser user = mapper.convert(registrationBody, LocalUser.class);

//        LocalUser user = new LocalUser();
//        user.setFirstName(registrationBody.getFirstName());
//        user.setLastName(registrationBody.getLastName());
//        user.setEmail(registrationBody.getEmail());
//        user.setUsername(registrationBody.getUsername());
//        user.setPassword(encryptionService.encryptPassword(registrationBody.getPassword()));

        user.setPassword(encryptionService.encryptPassword(registrationBody.password()));

        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
//        verificationTokenRepository.save(verificationToken);
        userRepository.save(user);
        return registrationBody;

    }

    private VerificationToken createVerificationToken(LocalUser user){
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerifiedJWT(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }

    @Override
    public String LoginUser(LoginBody loginBody) throws EcommerceProjectException {

        // check if user exists in database
        Optional<LocalUser> opUser = userRepository.findByUsernameIgnoreCase(loginBody.username());

        if (opUser.isPresent()) {
            LocalUser user = opUser.get();
            // verify login password is the same as the password in database
            if (encryptionService.verifyPassword(loginBody.password(), user.getPassword())) {
                return jwtService.generateJWT(user);
            }
        }

        return null;
    }
}
