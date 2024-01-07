package com.latif.ecommercebackend.exceptions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class ExceptionWrapper {
    private LocalDateTime timestamp;
    private Integer status;
    private String message;
    private String path;
    private Integer count;
    private List<ValidationError> validationErrorList;
}
