package br.com.olik.asigntest.service.strategy;

import br.com.olik.asigntest.entity.Wallet;

import java.math.BigDecimal;

public interface TransactionValidationStrategy {

    void validate(Wallet wallet, BigDecimal amout);
}
