package com.latif.ecommercebackend.service.impl;

import com.latif.ecommercebackend.dto.LoginBody;
import com.latif.ecommercebackend.dto.RegistrationBody;
import com.latif.ecommercebackend.exceptions.EcommerceProjectException;
import com.latif.ecommercebackend.exceptions.EmailFailureException;
import com.latif.ecommercebackend.exceptions.UserNotFoundException;
import com.latif.ecommercebackend.exceptions.UserNotVerifiedException;
import com.latif.ecommercebackend.mapper.MapperUtil;
import com.latif.ecommercebackend.model.User;

import com.latif.ecommercebackend.model.VerificationToken;
import com.latif.ecommercebackend.repository.UserRepository;
import com.latif.ecommercebackend.repository.VerificationTokenRepository;
import com.latif.ecommercebackend.service.EncryptionService;
import com.latif.ecommercebackend.service.JwtService;
import com.latif.ecommercebackend.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EncryptionService encryptionService;
    private final JwtService jwtService;
    private final EmailServiceImpl emailService;
    private final VerificationTokenRepository verificationTokenRepository;

    public UserServiceImpl(UserRepository userRepository,
                           EncryptionService encryptionService,
                           MapperUtil mapper,
                           JwtService jwtService,
                           EmailServiceImpl emailService,
                           VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.verificationTokenRepository = verificationTokenRepository;
    }


    @Override
    public RegistrationBody registerUser(RegistrationBody registrationBody) throws EcommerceProjectException, EmailFailureException {

        boolean isEmailExist = userRepository.findByEmailIgnoreCase(registrationBody.email()).isPresent();
        boolean isUsernameExist = userRepository.findByUsernameIgnoreCase(registrationBody.username()).isPresent();

        // check if email or username already exists in database
        if (isEmailExist || isUsernameExist) {
            throw new EcommerceProjectException("User already exists");
        }


        User user = new User();
        user.setFirstName(registrationBody.firstName());
        user.setLastName(registrationBody.lastName());
        user.setEmail(registrationBody.email());
        user.setUsername(registrationBody.username());
        user.setPassword(encryptionService.encryptPassword(registrationBody.password()));

        user.setPassword(encryptionService.encryptPassword(registrationBody.password()));

        VerificationToken verificationToken = createVerificationToken(user);
        emailService.sendVerificationEmail(verificationToken);
//        verificationTokenRepository.save(verificationToken);
        // token will be saved in database. handled by cascadeType.ALL
        userRepository.save(user);
        return registrationBody;

    }

    private VerificationToken createVerificationToken(User user) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(jwtService.generateVerifiedJWT(user));
        verificationToken.setCreatedTimestamp(new Timestamp(System.currentTimeMillis()));
        verificationToken.setUser(user);
        user.getVerificationTokens().add(verificationToken);
        return verificationToken;
    }


    @Override
    public String LoginUser(LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException, UserNotFoundException {

        // Check if user exists
        Optional<User> opUser = userRepository.findByUsernameIgnoreCase(loginBody.username());
        if (opUser.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        User user = opUser.get();

        // Verify password
        if (!encryptionService.verifyPassword(loginBody.password(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        // Check email verification
        if (!user.getIsEmailVerified()) {
            handleUnverifiedEmail(user);
        }

        return jwtService.generateJWT(user);
    }

    private void handleUnverifiedEmail(User user) throws UserNotVerifiedException, EmailFailureException {
        List<VerificationToken> verificationTokens = user.getVerificationTokens();

        boolean outDatedToken = verificationTokens.get(0)
                .getCreatedTimestamp()
                .before(new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60));


        boolean resend = verificationTokens.isEmpty() || outDatedToken;

        // check token date is valid and token is not empty
        if (!resend) {
            VerificationToken verificationToken = createVerificationToken(user);
            verificationTokenRepository.save(verificationToken);
            emailService.sendVerificationEmail(verificationToken);
        }
        throw new UserNotVerifiedException(resend);

    }

    @Transactional
    public void verifyUser(String token) throws UserNotVerifiedException {

        // find token in database
        Optional<VerificationToken> opToken = verificationTokenRepository.findByToken(token);

        if (opToken.isEmpty()) {
            throw new UserNotVerifiedException(false);
        }


        VerificationToken verificationToken = opToken.get();
        User user = verificationToken.getUser(); // get user from token

        if (!user.getIsEmailVerified()) {
            user.setIsEmailVerified(true);
            userRepository.save(user);
            // delete all existing tokens because we don't need them after the user is verified
            verificationTokenRepository.deleteByUser(user);

        }


    }
}









