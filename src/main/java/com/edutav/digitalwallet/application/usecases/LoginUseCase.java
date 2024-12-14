package com.edutav.digitalwallet.application.usecases;

import com.edutav.digitalwallet.application.dtos.LoginInputDTO;
import com.edutav.digitalwallet.domain.entities.User;
import com.edutav.digitalwallet.domain.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

public class LoginUseCase {

    private final UserRepository userRepository;
    private final String jwtSecret;
    private final long jwtExpirationMs;
    private final PasswordEncoder passwordEncoder;

    public LoginUseCase(
            UserRepository userRepository,
            String jwtSecret,
            long jwtExpirationMs,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<String> execute(LoginInputDTO loginInputDTO) {
        String encodedPassword = this.passwordEncoder.encode(loginInputDTO.getPassword());
        Optional<User> user = this.userRepository.findByUsername(
                loginInputDTO.getUsername()
        );
        if (user.isPresent() && passwordEncoder.matches(loginInputDTO.getPassword(), user.get().getPassword())) {
            String token = Jwts.builder()
                    .setSubject(user.get().getId().toString())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();
            return Optional.of(token);
        }

        return Optional.empty();
    }
}
