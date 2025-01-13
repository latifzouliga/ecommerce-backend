package com.latif.ecommercebackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record LoginBody(


        @Schema(description = "example@email.com")
        @NotNull(message = "Username is required")
        @NotBlank(message = "Username cannot be blank")
        String username,

        @Schema(description = "Qw123!abc")
        @NotNull(message = "Password is required")
        @NotBlank(message = "Password cannot be blank")
        @Size(
                min = 8,
                message = "Password must be at least 8 characters long"
        )


        @Pattern(
                regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).*",
                message = """ 
                        Password must contain at least one lowercase letter, one uppercase letter, 
                        one digit, and one special character.
                        """
        )
        String password
) {
}
