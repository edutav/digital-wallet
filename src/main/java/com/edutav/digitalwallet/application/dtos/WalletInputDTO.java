package com.edutav.digitalwallet.application.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WalletInputDTO {

    @Schema(example = "100.0")
    @Min(value = 0, message = "Initial balance must be non-negative")
    private BigDecimal initialBalance;

}
