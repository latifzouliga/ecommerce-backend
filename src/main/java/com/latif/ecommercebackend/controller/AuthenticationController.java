package com.latif.ecommercebackend.controller;

import com.latif.ecommercebackend.dto.LoginBody;
import com.latif.ecommercebackend.dto.LoginResponse;
import com.latif.ecommercebackend.dto.RegistrationBody;
import com.latif.ecommercebackend.exceptions.EcommerceProjectException;
import com.latif.ecommercebackend.exceptions.EmailFailureException;
import com.latif.ecommercebackend.exceptions.UserNotVerifiedException;
import com.latif.ecommercebackend.model.LocalUser;
import com.latif.ecommercebackend.model.ResponseWrapper;
import com.latif.ecommercebackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody) throws UserNotVerifiedException, EmailFailureException, EcommerceProjectException {


       String jwt = userService.LoginUser(loginBody);
        if (jwt == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        LoginResponse response = new LoginResponse();
        response.setJwt(jwt);
        response.setSuccess(true);
        return ResponseEntity.ok(response);


    }


    // test user that is loaded to SecurityContext
    @GetMapping("/me")
    public LocalUser getLoggedInUserProfile(@AuthenticationPrincipal LocalUser user){
//        LocalUser localUser = (LocalUser) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        return user;
    }

    @PostMapping("/verify")
    public ResponseEntity verifyEmail(@RequestParam String token){
        if (userService.verifyUser(token)){
            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}














