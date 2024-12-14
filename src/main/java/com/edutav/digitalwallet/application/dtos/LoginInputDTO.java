package com.edutav.digitalwallet.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginInputDTO {

    @Schema(example = "john_doe")
    @NotBlank(message = "Username is mandatory")
    private String username;

    @Schema(example = "password123")
    @NotBlank(message = "Password is mandatory")
    private String password;

}
