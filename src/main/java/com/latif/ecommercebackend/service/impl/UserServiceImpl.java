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
    private final MapperUtil mapper;
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

//        LocalUser user = mapper.convert(registrationBody, LocalUser.class);

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

        // check if user exists in database
        Optional<User> opUser = userRepository.findByUsernameIgnoreCase(loginBody.username());

        if (opUser.isPresent()) {   // if user is valid
            User user = opUser.get();
            // verify login password is the same as the password in database
            if (encryptionService.verifyPassword(loginBody.password(), user.getPassword())) {
                if (user.getIsEmailVerified()) {  // if email is verified continue as normal
                    return jwtService.generateJWT(user);
                } else { // if email is not verified
                    List<VerificationToken> verificationTokens = user.getVerificationTokens();

                    boolean outDatedToken = verificationTokens.get(0)
                            .getCreatedTimestamp()
                            .before(
//                                    Timestamp.from(Instant.now().minus(1, ChronoUnit.HOURS))
                                    new Timestamp(System.currentTimeMillis() - 1000 * 60 * 60)
                            );
                    // if no verification is sent OR if verification is outdated by one hour
                    boolean resend = verificationTokens.isEmpty() || outDatedToken;

                    if (resend) { // resend new verification email
                        VerificationToken verificationToken = createVerificationToken(user);
                        verificationTokenRepository.save(verificationToken);
                        emailService.sendVerificationEmail(verificationToken);
                    }
                    throw new UserNotVerifiedException(resend);
                }
            }
        }
        throw new UserNotFoundException("User not found");

    }

    @Transactional
    public void verifyUser(String token) throws UserNotVerifiedException {

        // find token in database
        Optional<VerificationToken> opToken = verificationTokenRepository.findByToken(token);

        if (opToken.isPresent()) {
            VerificationToken verificationToken = opToken.get();
            User user = verificationToken.getUser(); // get user from token

            if (!user.getIsEmailVerified()) {
                user.setIsEmailVerified(true);
                userRepository.save(user);
                verificationTokenRepository.deleteByUser(user); // delete all existing tokens because we don't need them after the user is verified

            }

        } else {
            throw new UserNotVerifiedException(false);
        }

    }
}










