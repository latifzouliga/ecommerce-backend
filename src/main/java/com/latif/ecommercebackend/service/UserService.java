package com.latif.ecommercebackend.service;

import com.latif.ecommercebackend.dto.LoginBody;
import com.latif.ecommercebackend.dto.RegistrationBody;
import com.latif.ecommercebackend.exceptions.EcommerceProjectException;
import com.latif.ecommercebackend.exceptions.EmailFailureException;

public interface UserService {

    RegistrationBody registerUser(RegistrationBody registrationBody) throws EcommerceProjectException, EmailFailureException;

    String LoginUser(LoginBody loginBody) throws EcommerceProjectException;
}
