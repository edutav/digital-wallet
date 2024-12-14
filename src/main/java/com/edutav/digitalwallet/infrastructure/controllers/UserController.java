package com.edutav.digitalwallet.infrastructure.controllers;

import com.edutav.digitalwallet.application.dtos.LoginInputDTO;
import com.edutav.digitalwallet.application.dtos.ResponseAPI;
import com.edutav.digitalwallet.application.dtos.UserInputDTO;
import com.edutav.digitalwallet.application.usecases.LoginUseCase;
import com.edutav.digitalwallet.application.usecases.RegisterUserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Operations related to users")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;

    public UserController(
            RegisterUserUseCase registerUserUseCase,
            LoginUseCase loginUseCase
    ) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUseCase = loginUseCase;
    }

    @PostMapping
    @Operation(summary = "Register a new user", description = "Registers a new user with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<ResponseAPI> registerUser(@Valid @RequestBody UserInputDTO dto) {
        try {
            this.registerUserUseCase.execute(dto);
            ResponseAPI response = new ResponseAPI(
                    "User registered successfully",
                    HttpStatus.CREATED.value(),
                    null
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ResponseAPI response = new ResponseAPI(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user", description = "Logs in a user with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully", content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<ResponseAPI> loginUser(@Valid @RequestBody LoginInputDTO dto) {
        try {
            Optional<String> token = this.loginUseCase.execute(dto);
            if (token.isPresent()) {
                ResponseAPI response = new ResponseAPI(
                        "User logged in successfully",
                        HttpStatus.OK.value(),
                        token.get()
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                ResponseAPI response = new ResponseAPI(
                        "Invalid credentials",
                        HttpStatus.UNAUTHORIZED.value(),
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            ResponseAPI response = new ResponseAPI(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
