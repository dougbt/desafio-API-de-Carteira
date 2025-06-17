package br.com.olik.asigntest.validation;

import br.com.olik.asigntest.entity.Wallet;
import br.com.olik.asigntest.exception.BusinessException;
import br.com.olik.asigntest.service.strategy.ItauTransactionValidation;
import br.com.olik.asigntest.service.strategy.TransactionValidationFactory;
import br.com.olik.asigntest.service.strategy.TransactionValidationStrategy;
import br.com.olik.asigntest.service.strategy.WithouTransactionValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
 public class TransactionValidationFactoryTest {

    @InjectMocks
    private TransactionValidationFactory transactionValidationFactory;

    private ItauTransactionValidation itauTransactionValidation;

    @BeforeEach
    void setUp() {
        itauTransactionValidation = new ItauTransactionValidation();
    }

    @Test
    void getStrategyReturnsItauTransactionValidationForItauWalletType() {
        Wallet.WalletType walletType = Wallet.WalletType.ITAU;

        TransactionValidationStrategy strategy = transactionValidationFactory.getStrategy(walletType);

        assertInstanceOf(ItauTransactionValidation.class, strategy);
    }

    @Test
    void getStrategyReturnsWithouTransactionValidationForSantanderWalletType() {
        Wallet.WalletType walletType = Wallet.WalletType.SANTANDER;

        TransactionValidationStrategy strategy = transactionValidationFactory.getStrategy(walletType);

        assertInstanceOf(WithouTransactionValidation.class, strategy);
    }

    @Test
    void validateThrowsBusinessExceptionWhenNewAmountIsNegative() {
        Wallet wallet = new Wallet();
        BigDecimal newAmount = BigDecimal.valueOf(-10);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> itauTransactionValidation.validate(wallet, newAmount));

        assertEquals("ITAU does not allow negative balance", exception.getMessage());
    }

    @Test
    void validateThrowsBusinessExceptionWhenNewAmountIsZero() {
        Wallet wallet = new Wallet();
        BigDecimal newAmount = BigDecimal.ZERO;

        BusinessException exception = assertThrows(BusinessException.class,
                () -> itauTransactionValidation.validate(wallet, newAmount));

        assertEquals("ITAU does not allow negative balance", exception.getMessage());
    }

    @Test
    void validateDoesNotThrowExceptionWhenNewAmountIsPositive() {
        Wallet wallet = new Wallet();
        BigDecimal newAmount = BigDecimal.valueOf(100);

        assertDoesNotThrow(() -> itauTransactionValidation.validate(wallet, newAmount));
    }
}