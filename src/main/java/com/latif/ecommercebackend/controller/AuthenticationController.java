package com.latif.ecommercebackend.controller;

import com.latif.ecommercebackend.dto.LoginBody;
import com.latif.ecommercebackend.dto.LoginResponse;
import com.latif.ecommercebackend.dto.RegistrationBody;
import com.latif.ecommercebackend.exceptions.EcommerceProjectException;
import com.latif.ecommercebackend.exceptions.EmailFailureException;
import com.latif.ecommercebackend.exceptions.UserNotFoundException;
import com.latif.ecommercebackend.exceptions.UserNotVerifiedException;
import com.latif.ecommercebackend.model.ResponseWrapper;
import com.latif.ecommercebackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    /*
    {
        "firstName": "latif",
        "lastName": "zouli",
        "email": "zoul@yallo.com",
        "username": "latif",
        "password": "Abc123"
    }
     */
    @PostMapping("/register")
    public ResponseEntity<ResponseWrapper> registerUser(@Valid @RequestBody RegistrationBody registrationBody) throws EcommerceProjectException, EmailFailureException {

        RegistrationBody user = userService.registerUser(registrationBody);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ResponseWrapper.builder()
                                .success(true)
                                .status(HttpStatus.CREATED)
                                .message("User registration successful")
                                .data(user)
                                .build()
                );

    }

    /*
    {
        "username": "latif",
        "password": "Abc1"
     }
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException, EcommerceProjectException, UserNotFoundException {


        String jwt = userService.LoginUser(loginBody);

        LoginResponse response = LoginResponse.builder()
                .jwt(jwt)
                .success(true)
                .build();
        return ResponseEntity.ok(response);


    }


    // test user that is loaded to SecurityContext
    @GetMapping("/me")
    public ResponseEntity<String> getLoggedInUserProfile(Principal principal){

       return ResponseEntity.ok("The login user : " + principal.getName());

    }

    @PostMapping("/verify")
    public ResponseEntity<ResponseWrapper> verifyEmail(@RequestParam String token) throws UserNotVerifiedException {
        userService.verifyUser(token);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .message("Verification successful")
                .success(true)
                .build());
    }
}














