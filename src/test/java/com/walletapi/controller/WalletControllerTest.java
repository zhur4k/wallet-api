package com.walletapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walletapi.dto.WalletOperationRequest;
import com.walletapi.model.OperationType;
import com.walletapi.model.Wallet;
import com.walletapi.service.WalletService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WalletService walletService;

    @Test
    void testPerformOperation() throws Exception {
        WalletOperationRequest request = new WalletOperationRequest(
                UUID.randomUUID(),
                OperationType.WITHDRAW,
                BigDecimal.valueOf(100.50)
        );

        doNothing().when(walletService).processOperation(ArgumentMatchers.any(WalletOperationRequest.class));

        mockMvc.perform(post("/api/v1/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Operation successful"));

        verify(walletService, times(1)).processOperation(any(WalletOperationRequest.class));
    }

    @Test
    void testGetBalance() throws Exception {
        UUID walletId = UUID.randomUUID();
        BigDecimal balance = BigDecimal.valueOf(500.75);

        when(walletService.getBalance(walletId)).thenReturn(balance);

        mockMvc.perform(get("/api/v1/wallets/{walletId}", walletId))
                .andExpect(status().isOk())
                .andExpect(content().string(balance.toString()));

        verify(walletService, times(1)).getBalance(walletId);
    }

    @Test
    void testGetAllWallets() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setId(UUID.randomUUID());
        wallet.setBalance(BigDecimal.valueOf(100.00));
        List<Wallet> wallets = Collections.singletonList(wallet);

        when(walletService.getAll()).thenReturn(wallets);

        mockMvc.perform(get("/api/v1/wallets/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(wallets)));

        verify(walletService, times(1)).getAll();
    }

    @Test
    void testCreateWallet() throws Exception {
        UUID walletId = UUID.randomUUID();

        when(walletService.createWallet()).thenReturn(walletId);

        mockMvc.perform(get("/api/v1/wallets/create"))
                .andExpect(status().isOk())
                .andExpect(content().json("\"" + walletId + "\""));

        verify(walletService, times(1)).createWallet();
    }
}
