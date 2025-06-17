package br.com.olik.asigntest.service;

import br.com.olik.asigntest.dto.TransactionDto;

import java.math.BigDecimal;

public interface TransactionalService {

    BigDecimal getAmount(Long userId);

    BigDecimal processTransaction(TransactionDto transactionDto);
}
