package com.edutav.digitalwallet.infrastructure.controllers;

import com.edutav.digitalwallet.application.dtos.DepositFundsDTO;
import com.edutav.digitalwallet.application.dtos.ResponseAPI;
import com.edutav.digitalwallet.application.dtos.WalletInputDTO;
import com.edutav.digitalwallet.application.usecases.CreateWalletUseCase;
import com.edutav.digitalwallet.application.usecases.DepositFundsUseCase;
import com.edutav.digitalwallet.application.usecases.GetHistoricalBalanceUseCase;
import com.edutav.digitalwallet.application.usecases.GetWalletBalanceUseCase;
import com.edutav.digitalwallet.application.usecases.TransferFundsUseCase;
import com.edutav.digitalwallet.application.usecases.WithdrawFundsUseCase;
import com.edutav.digitalwallet.infrastructure.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/wallets")
@Tag(name = "Wallets", description = "Operations related to wallets")
public class WalletController {

    private final CreateWalletUseCase createWalletUseCase;
    private final JwtUtils jwtUtils;
    private final GetWalletBalanceUseCase getWalletBalanceUseCase;
    private final DepositFundsUseCase depositFundsUseCase;
    private final WithdrawFundsUseCase withdrawFundsUseCase;
    private final TransferFundsUseCase transferFundsUseCase;
    private final GetHistoricalBalanceUseCase getHistoricalBalanceUseCase;

    public WalletController(
            CreateWalletUseCase createWalletUseCase,
            JwtUtils jwtUtils,
            GetWalletBalanceUseCase getWalletBalanceUseCase,
            DepositFundsUseCase depositFundsUseCase,
            WithdrawFundsUseCase withdrawFundsUseCase,
            TransferFundsUseCase transferFundsUseCase,
            GetHistoricalBalanceUseCase getHistoricalBalanceUseCase
    ) {
        this.createWalletUseCase = createWalletUseCase;
        this.jwtUtils = jwtUtils;
        this.getWalletBalanceUseCase = getWalletBalanceUseCase;
        this.depositFundsUseCase = depositFundsUseCase;
        this.withdrawFundsUseCase = withdrawFundsUseCase;
        this.transferFundsUseCase = transferFundsUseCase;
        this.getHistoricalBalanceUseCase = getHistoricalBalanceUseCase;
    }

    @PostMapping
    @Operation(
            summary = "Create a new wallet",
            description = "Creates a new wallet for the authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Wallet created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseAPI> createWallet(
            @Valid @RequestBody WalletInputDTO dto,
            @RequestHeader("Authorization") String token
    ) {
        try {
            String jwtToken = token.substring(7);
            String userID = jwtUtils.getUserNameFromJwtToken(jwtToken);

            createWalletUseCase.execute(userID, dto.getInitialBalance());
            ResponseAPI response = new ResponseAPI(
                    "Wallet created successfully",
                    HttpStatus.CREATED.value(),
                    null
            );

            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            ResponseAPI response = new ResponseAPI(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/{userID}/balance")
    @Operation(
            summary = "Get wallet balance",
            description = "Retrieves the balance of the wallet for the authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseAPI> getWalletBalance(
            @PathVariable String userID,
            @RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.substring(7);
            String id = jwtUtils.getUserNameFromJwtToken(jwtToken);

            if (!id.equals(userID)) {
                ResponseAPI response = new ResponseAPI(
                        "Unauthorized",
                        HttpStatus.UNAUTHORIZED.value(),
                        null
                );
                return ResponseEntity.status(401).body(response);
            }

            BigDecimal balance = this.getWalletBalanceUseCase.execute(userID);
            ResponseAPI response = new ResponseAPI(
                    "Balance retrieved successfully",
                    HttpStatus.OK.value(),
                    balance
            );
            return ResponseEntity.status(200).body(response);
        } catch (RuntimeException e) {
            ResponseAPI response = new ResponseAPI(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND.value(),
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ResponseAPI response = new ResponseAPI(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/{userID}/deposit")
    @Operation(
            summary = "Deposit funds",
            description = "Deposits funds into the wallet of the authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funds deposited successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseAPI> depositFounds(
            @PathVariable String userID,
            @Valid @RequestBody DepositFundsDTO dto,
            @RequestHeader("Authorization") String token
    ) {
        try {
            String jwtToken = token.substring(7);
            String id = jwtUtils.getUserNameFromJwtToken(jwtToken);

            if (!id.equals(userID)) {
                ResponseAPI response = new ResponseAPI(
                        "Unauthorized",
                        HttpStatus.UNAUTHORIZED.value(),
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(response);
            }

            depositFundsUseCase.execute(userID, dto.getAmount());
            ResponseAPI response = new ResponseAPI(
                    "Funds deposited successfully",
                    HttpStatus.OK.value(),
                    null
            );
            return ResponseEntity.status(HttpStatus.OK.value()).body(response);
        } catch (RuntimeException e) {
            ResponseAPI response = new ResponseAPI(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND.value(),
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ResponseAPI response = new ResponseAPI(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{userID}/withdraw")
    @Operation(
            summary = "Withdraw funds",
            description = "Withdraws funds from the wallet of the authenticated user.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funds withdrawn successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseAPI> withdrawFunds(
            @PathVariable String userID,
            @Valid @RequestBody DepositFundsDTO dto,
            @RequestHeader("Authorization") String token
    ) {
        try {
            String jwtToken = token.substring(7);
            String id = jwtUtils.getUserNameFromJwtToken(jwtToken);

            if (!id.equals(userID)) {
                ResponseAPI response = new ResponseAPI(
                        "Unauthorized",
                        HttpStatus.UNAUTHORIZED.value(),
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(response);
            }

            this.withdrawFundsUseCase.execute(userID, dto.getAmount());
            ResponseAPI response = new ResponseAPI(
                    "Funds withdrawn successfully",
                    HttpStatus.OK.value(),
                    null
            );
            return ResponseEntity.status(HttpStatus.OK.value()).body(response);
        } catch (RuntimeException e) {
            ResponseAPI response = new ResponseAPI(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND.value(),
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ResponseAPI response = new ResponseAPI(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{fromUserID}/transfer/{toUserID}")
    @Operation(
            summary = "Transfer funds",
            description = "Transfers funds from the wallet of the authenticated user to another user's wallet.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funds transferred successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseAPI> transferFunds(
            @PathVariable String fromUserID,
            @PathVariable String toUserID,
            @Valid @RequestBody DepositFundsDTO dto,
            @RequestHeader("Authorization") String token
    ) {
        try {
            String jwtToken = token.substring(7);
            String id = jwtUtils.getUserNameFromJwtToken(jwtToken);

            if (!id.equals(fromUserID)) {
                ResponseAPI response = new ResponseAPI(
                        "Unauthorized",
                        HttpStatus.UNAUTHORIZED.value(),
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(response);
            }

            transferFundsUseCase.execute(fromUserID, toUserID, dto.getAmount());
            ResponseAPI response = new ResponseAPI(
                    "Funds transferred successfully",
                    HttpStatus.OK.value(),
                    null
            );
            return ResponseEntity.status(HttpStatus.OK.value()).body(response);
        } catch (RuntimeException e) {
            ResponseAPI response = new ResponseAPI(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND.value(),
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ResponseAPI response = new ResponseAPI(
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userID}/balance/history")
    @Operation(
            summary = "Get historical wallet balance",
            description = "Retrieves the balance of the wallet for the authenticated user at a specific point in the past.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historical balance retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseAPI> getHistoricalBalance(
            @PathVariable String userID,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime date,
            @RequestHeader("Authorization") String token
    ) {
        try {
            String jwtToken = token.substring(7);
            String id = jwtUtils.getUserNameFromJwtToken(jwtToken);

            if (!id.equals(userID)) {
                ResponseAPI response = new ResponseAPI(
                        "Unauthorized",
                        HttpStatus.UNAUTHORIZED.value(),
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(response);
            }

            BigDecimal balance = this.getHistoricalBalanceUseCase.execute(userID, date);
            ResponseAPI response = new ResponseAPI(
                    "Historical balance retrieved successfully",
                    HttpStatus.OK.value(),
                    balance
            );
            return ResponseEntity.status(HttpStatus.OK.value()).body(response);
        } catch (RuntimeException e) {
            ResponseAPI response = new ResponseAPI(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND.value(),
                    null
            );
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
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
