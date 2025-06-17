package br.com.olik.asigntest.service.impl;

import br.com.olik.asigntest.dto.TransactionDto;
import br.com.olik.asigntest.entity.Wallet;
import br.com.olik.asigntest.exception.BusinessException;
import br.com.olik.asigntest.repository.WalletRepository;
import br.com.olik.asigntest.service.TransactionalService;
import br.com.olik.asigntest.service.strategy.TransactionValidationFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionalServiceImpl  implements TransactionalService {

    private final WalletRepository walletRepository;
    private final TransactionValidationFactory transactionValidationFactory ;

    @Override
    public BigDecimal getAmount(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId);
        if(wallet == null){
            throw new BusinessException("Wallet not found for userId: " + userId);
        }

        return wallet.getAmount();
    }

    @Override
    public BigDecimal processTransaction(TransactionDto transactionDto) {
        Wallet wallet = walletRepository.findByUserId(transactionDto.getUserId());
        if (wallet == null) {
            throw new BusinessException("Wallet not found for userId: " + transactionDto.getUserId());
        }

        BigDecimal newAmount = wallet.getAmount().add(transactionDto.getAmount());

        transactionValidationFactory.getStrategy(wallet.getType()).validate(wallet, newAmount);

        wallet.setAmount(newAmount);

        walletRepository.save(wallet);

        log.info("Processed transaction for userId {} | newAmount: {}", wallet.getUserId(), wallet.getAmount());

        return wallet.getAmount();
    }
}
