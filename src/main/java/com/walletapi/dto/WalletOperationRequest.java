package com.walletapi.dto;

import com.walletapi.model.OperationType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletOperationRequest (
        @NotNull UUID walletId,
        @NotNull OperationType operationType,
        @DecimalMin(value = "0.01", message = "Amount must be greater than zero") BigDecimal amount
){
}
