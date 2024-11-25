package com.walletapi.service;

import com.walletapi.dto.WalletOperationRequest;
import com.walletapi.model.Wallet;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface WalletService {
    BigDecimal getBalance(UUID id);
    void processOperation(WalletOperationRequest operationRequest);
    UUID createWallet();
    List<Wallet> getAll();
}
