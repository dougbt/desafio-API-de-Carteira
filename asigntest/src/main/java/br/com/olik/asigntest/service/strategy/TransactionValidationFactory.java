package br.com.olik.asigntest.service.strategy;

import br.com.olik.asigntest.entity.Wallet;
import br.com.olik.asigntest.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionValidationFactory {

    public TransactionValidationStrategy getStrategy(Wallet.WalletType walletType) {

        return switch (walletType) {
            case ITAU -> new ItauTransactionValidation();
            case SANTANDER -> new WithouTransactionValidation();
            default -> throw new BusinessException("Unsupported wallet type: " + walletType);
        };
    }
}
