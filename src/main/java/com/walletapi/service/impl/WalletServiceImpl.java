package com.walletapi.service.impl;

import com.walletapi.dto.WalletOperationRequest;
import com.walletapi.exception.WalletNotFoundException;
import com.walletapi.model.OperationType;
import com.walletapi.model.Wallet;
import com.walletapi.repository.WalletRepository;
import com.walletapi.service.WalletService;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;

    @Override
    public BigDecimal getBalance(UUID id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new WalletNotFoundException(id))
                .getBalance();
    }

    @Override
    @Transactional
    public void processOperation(WalletOperationRequest operationRequest) {
        boolean success = false;

        while (!success) {
            try {
                Wallet wallet = walletRepository.findById(operationRequest.walletId())
                        .orElseThrow(() -> new WalletNotFoundException(operationRequest.walletId()));

                if (operationRequest.operationType() == OperationType.WITHDRAW) {
                    if (wallet.getBalance().compareTo(operationRequest.amount()) < 0) {
                        throw new IllegalArgumentException("Not enough funds");
                    }
                    wallet.setBalance(wallet.getBalance().subtract(operationRequest.amount()));
                    log.info("Withdrawn {} from wallet with ID {}", operationRequest.amount(), operationRequest.walletId());
                } else {
                    wallet.setBalance(wallet.getBalance().add(operationRequest.amount()));
                    log.info("Deposited {} to wallet with ID {}", operationRequest.amount(), operationRequest.walletId());
                }

                walletRepository.save(wallet);
                success = true;
            } catch (OptimisticLockException e) {
                log.warn("Optimistic lock exception occurred, retrying...", e);
            }
        }
    }

    @Override
    public UUID createWallet() {
        Wallet wallet = new Wallet();
        wallet.setId(UUID.randomUUID());
        wallet.setBalance(BigDecimal.ZERO);
        return walletRepository.save(wallet).getId();
    }

    @Override
    public List<Wallet> getAll() {
        return walletRepository.findAll();
    }
}
