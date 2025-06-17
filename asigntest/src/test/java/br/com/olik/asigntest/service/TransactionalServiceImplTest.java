package br.com.olik.asigntest.service;

import br.com.olik.asigntest.dto.TransactionDto;
import br.com.olik.asigntest.entity.Wallet;
import br.com.olik.asigntest.exception.BusinessException;
import br.com.olik.asigntest.repository.WalletRepository;
import br.com.olik.asigntest.service.impl.TransactionalServiceImpl;
import br.com.olik.asigntest.service.strategy.TransactionValidationFactory;
import br.com.olik.asigntest.service.strategy.TransactionValidationStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionalServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionValidationFactory transactionValidationFactory;

    @Mock
    private TransactionValidationStrategy transactionValidationStrategy;

    @InjectMocks
    private TransactionalServiceImpl transactionalService;

    @Test
    void getAmountReturnsWalletAmountWhenWalletExists() {
        Long userId = 1L;
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setAmount(BigDecimal.valueOf(100));

        when(walletRepository.findByUserId(userId)).thenReturn(wallet);

        BigDecimal amount = transactionalService.getAmount(userId);

        assertEquals(BigDecimal.valueOf(100), amount);
    }

    @Test
    void getAmountThrowsBusinessExceptionWhenWalletNotFound() {
        Long userId = 1L;

        when(walletRepository.findByUserId(userId)).thenReturn(null);

        assertThrows(BusinessException.class, () -> transactionalService.getAmount(userId));
    }

    @Test
    void processTransactionUpdatesWalletAmountWhenValid() {
        Long userId = 1L;
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setAmount(BigDecimal.valueOf(100));
        wallet.setType(Wallet.WalletType.ITAU);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setUserId(userId);
        transactionDto.setAmount(BigDecimal.valueOf(50));

        when(walletRepository.findByUserId(userId)).thenReturn(wallet);
        when(transactionValidationFactory.getStrategy(Wallet.WalletType.ITAU)).thenReturn(transactionValidationStrategy);

        BigDecimal updatedAmount = transactionalService.processTransaction(transactionDto);

        assertEquals(BigDecimal.valueOf(150), updatedAmount);
        verify(walletRepository).save(wallet);
        verify(transactionValidationStrategy).validate(wallet, BigDecimal.valueOf(150));
    }

    @Test
    void processTransactionThrowsBusinessExceptionWhenWalletNotFound() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setUserId(1L);
        transactionDto.setAmount(BigDecimal.valueOf(50));

        when(walletRepository.findByUserId(transactionDto.getUserId())).thenReturn(null);

        assertThrows(BusinessException.class, () -> transactionalService.processTransaction(transactionDto));
    }
}