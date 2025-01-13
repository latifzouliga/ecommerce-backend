package com.latif.ecommercebackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record RegistrationBody(

        @Schema(example = "John")
        @NotNull(message = "First name is required")
        @NotBlank(message = "First name cannot be blank")
        @Size(
                min = 2,
                max = 255,
                message = "First name must be between 2 and 255 characters"
        )
        String firstName,

        @Schema(example = "Doe")
        @NotNull(message = "Last name is required")
        @NotBlank(message = "Last name cannot be blank")
        @Size(
                min = 2,
                max = 255,
                message = "Last name must be between 2 and 255 characters"
        )
        String lastName,

        @Schema(example = "johndoe")
        @NotNull(message = "Username is required")
        @NotBlank(message = "Username cannot be blank")
        @Size(
                min = 2,
                max = 255,
                message = "Username must be between 2 and 255 characters"
        )
        String username,

        @Schema(example = "example@email.com")
        @Email(
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "Invalid email format"
        )
        @NotNull(message = "Email is required")
        @NotBlank(message = "Email cannot be blank")
        @Size(
                min = 6,
                max = 36,
                message = "Email must be between 6 and 36 characters"
        )
        String email,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotNull(message = "Password is required")
        @NotBlank(message = "Password cannot be blank")
        @Size(
                min = 6,
                max = 255,
                message = "Password must be between 6 and 255 characters")
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
