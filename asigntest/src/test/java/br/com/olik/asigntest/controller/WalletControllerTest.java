package br.com.olik.asigntest.controller;

import br.com.olik.asigntest.dto.TransactionDto;
import br.com.olik.asigntest.exception.BusinessException;
import br.com.olik.asigntest.service.TransactionalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletControllerTest {

    @Mock
    private TransactionalService transactionalService;

    @InjectMocks
    private WalletController walletController;

    @Test
    void getAmountReturnsAmountWhenUserIdIsValid() {
        Long userId = 1L;
        BigDecimal expectedAmount = BigDecimal.valueOf(100);

        when(transactionalService.getAmount(userId)).thenReturn(expectedAmount);

        ResponseEntity<BigDecimal> response = walletController.getAmount(userId);

        assertEquals(ResponseEntity.ok(expectedAmount), response);
    }

    @Test
    void getAmountThrowsExceptionWhenUserIdIsInvalid() {
        Long userId = 1L;

        when(transactionalService.getAmount(userId)).thenThrow(new BusinessException("Wallet not found"));

        assertThrows(BusinessException.class, () -> walletController.getAmount(userId));
    }

    @Test
    void transactionProcessesTransactionSuccessfully() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setUserId(1L);
        transactionDto.setAmount(BigDecimal.valueOf(50));
        BigDecimal updatedAmount = BigDecimal.valueOf(150);

        when(transactionalService.processTransaction(transactionDto)).thenReturn(updatedAmount);

        ResponseEntity<BigDecimal> response = walletController.transaction(transactionDto);

        assertEquals(ResponseEntity.ok(updatedAmount), response);
    }

    @Test
    void transactionThrowsExceptionWhenTransactionIsInvalid() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setUserId(1L);
        transactionDto.setAmount(BigDecimal.valueOf(50));

        when(transactionalService.processTransaction(transactionDto)).thenThrow(new BusinessException("Invalid transaction"));

        assertThrows(BusinessException.class, () -> walletController.transaction(transactionDto));
    }
}