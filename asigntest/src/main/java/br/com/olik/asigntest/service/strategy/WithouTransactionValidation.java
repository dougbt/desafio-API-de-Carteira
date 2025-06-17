package br.com.olik.asigntest.service.strategy;

import br.com.olik.asigntest.entity.Wallet;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WithouTransactionValidation implements TransactionValidationStrategy {

    @Override
    public void validate(Wallet wallet, BigDecimal newAmount) {
        // SANTANDER PERMITE VALORES POSITIVOS E NEGATIVOS
    }
}
