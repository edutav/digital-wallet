package com.edutav.digitalwallet.infrastructure.config;

import com.edutav.digitalwallet.application.usecases.CreateWalletUseCase;
import com.edutav.digitalwallet.application.usecases.DepositFundsUseCase;
import com.edutav.digitalwallet.application.usecases.GetHistoricalBalanceUseCase;
import com.edutav.digitalwallet.application.usecases.GetWalletBalanceUseCase;
import com.edutav.digitalwallet.application.usecases.LoginUseCase;
import com.edutav.digitalwallet.application.usecases.RegisterUserUseCase;
import com.edutav.digitalwallet.application.usecases.TransferFundsUseCase;
import com.edutav.digitalwallet.application.usecases.WithdrawFundsUseCase;
import com.edutav.digitalwallet.domain.repositories.TransactionRepository;
import com.edutav.digitalwallet.domain.repositories.UserRepository;
import com.edutav.digitalwallet.domain.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UseCaseConfig {

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new RegisterUserUseCase(userRepository, passwordEncoder);
    }

    @Bean
    public LoginUseCase loginUseCase(
            UserRepository userRepository,
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.expirationMs}") long jwtExpirationMs,
            PasswordEncoder passwordEncoder
    ) {
        return new LoginUseCase(userRepository, jwtSecret, jwtExpirationMs, passwordEncoder);
    }

    @Bean
    public CreateWalletUseCase createWalletUseCase(
            WalletRepository walletRepository,
            UserRepository userRepository,
            TransactionRepository transactionRepository
    ) {
        return new CreateWalletUseCase(walletRepository, userRepository, transactionRepository);
    }

    @Bean
    public GetWalletBalanceUseCase getWalletBalanceUseCase(WalletRepository walletRepository) {
        return new GetWalletBalanceUseCase(walletRepository);
    }

    @Bean
    public DepositFundsUseCase depositFundsUseCase(
            WalletRepository walletRepository,
            TransactionRepository transactionRepository
    ) {
        return new DepositFundsUseCase(walletRepository, transactionRepository);
    }

    @Bean
    public WithdrawFundsUseCase withdrawFundsUseCase(
            WalletRepository walletRepository,
            TransactionRepository transactionRepository
    ) {
        return new WithdrawFundsUseCase(walletRepository, transactionRepository);
    }

    @Bean
    public TransferFundsUseCase transferFundsUseCase(
            WalletRepository walletRepository,
            TransactionRepository transactionRepository
    ) {
        return new TransferFundsUseCase(walletRepository, transactionRepository);
    }

    @Bean
    public GetHistoricalBalanceUseCase getHistoricalBalanceUseCase(
            WalletRepository walletRepository,
            TransactionRepository transactionRepository
    ) {
        return new GetHistoricalBalanceUseCase(walletRepository, transactionRepository);
    }
}
