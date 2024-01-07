package com.latif.ecommercebackend.api.controller.auth;

import com.latif.ecommercebackend.dto.LoginBody;
import com.latif.ecommercebackend.dto.LoginResponse;
import com.latif.ecommercebackend.dto.RegistrationBody;
import com.latif.ecommercebackend.exceptions.EcommerceProjectException;
import com.latif.ecommercebackend.model.ResponseWrapper;
import com.latif.ecommercebackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseWrapper> registerUser(@Valid @RequestBody RegistrationBody registrationBody) throws EcommerceProjectException {

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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginBody loginBody) throws EcommerceProjectException {

        String jwt = userService.LoginUser(loginBody);

        if (jwt == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        LoginResponse response = new LoginResponse();
        response.setJwt(jwt);
        return ResponseEntity.ok(response);

    }
}