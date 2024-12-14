package com.edutav.digitalwallet.application.usecases;

import com.edutav.digitalwallet.application.dtos.UserInputDTO;
import com.edutav.digitalwallet.domain.entities.User;
import com.edutav.digitalwallet.domain.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void execute(UserInputDTO userInputDTO) {
        User user = new User();

        user.setEmail(userInputDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userInputDTO.getPassword()));
        user.setUsername(userInputDTO.getUsername());

        this.userRepository.save(user);
    }
}
