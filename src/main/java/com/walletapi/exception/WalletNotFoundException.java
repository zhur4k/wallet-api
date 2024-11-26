package com.walletapi.exception;

import java.util.UUID;

public class WalletNotFoundException extends IllegalArgumentException  {
    public WalletNotFoundException(UUID id) {
        super(String.format("Wallet with id %s not found", id));
    }
}
