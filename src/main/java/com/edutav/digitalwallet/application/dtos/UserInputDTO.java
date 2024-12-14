package com.edutav.digitalwallet.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInputDTO {

    @Schema(example = "john_doe")
    @NotBlank(message = "Name is mandatory")
    private String username;

    @Schema(example = "password123")
    @NotBlank(message = "Password is mandatory")
    private String password;

    @Schema(example = "john.doe@example.com")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

}
