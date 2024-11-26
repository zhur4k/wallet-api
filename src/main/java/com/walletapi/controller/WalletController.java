package com.walletapi.controller;

import com.walletapi.dto.WalletOperationRequest;
import com.walletapi.model.Wallet;
import com.walletapi.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<String> performOperation(@RequestBody @Valid WalletOperationRequest walletOperationRequest) {
        walletService.processOperation(walletOperationRequest);
        return ResponseEntity.ok("Operation successful");
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable UUID walletId) {
        return ResponseEntity.ok(walletService.getBalance(walletId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Wallet>> getAllWallets() {
        return ResponseEntity.ok(walletService.getAll());
    }

    @GetMapping("/create")
    public ResponseEntity<UUID> createWallet() {
        return ResponseEntity.ok(walletService.createWallet());
    }
}
