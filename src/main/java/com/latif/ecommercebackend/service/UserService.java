package com.latif.ecommercebackend.service;

import com.latif.ecommercebackend.dto.RegistrationBody;
import com.latif.ecommercebackend.exceptions.EcommerceProjectException;

public interface UserService {

    RegistrationBody registerUser(RegistrationBody registrationBody) throws EcommerceProjectException;
}
