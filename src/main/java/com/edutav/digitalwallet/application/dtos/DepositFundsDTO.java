package com.edutav.digitalwallet.application.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepositFundsDTO {

    @Min(value = 1, message = "Value must be greater than 0")
    @NotNull(message = "Amount is mandatory")
    private BigDecimal amount;

}
