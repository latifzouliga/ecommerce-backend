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
        @NotNull
        @NotBlank
        @Size(min = 2, max = 255)
        String firstName,

        @Schema(example = "Doe")
        @NotNull
        @NotBlank
        @Size(min = 2, max = 255)
        String lastName,

        @Schema(example = "johndoe")
        @NotNull
        @NotBlank
        @Size(min = 2, max = 255)
        String username,

        @Schema(example = "example@email.com")
        @Email
        @NotNull
        @NotBlank
        @Size(min = 6, max = 36)
        String email,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotNull
        @NotBlank
        @Size(min = 6, max = 255)
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$")
        String password
) {
}
