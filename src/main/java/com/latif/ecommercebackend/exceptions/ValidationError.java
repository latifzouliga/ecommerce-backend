package com.latif.ecommercebackend.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ValidationError {

    private String errorField;
    private Object rejectedValue;
    private String reason;
}
