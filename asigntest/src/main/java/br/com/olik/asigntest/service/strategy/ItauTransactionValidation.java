package br.com.olik.asigntest.service.strategy;

import br.com.olik.asigntest.entity.Wallet;
import br.com.olik.asigntest.exception.BusinessException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ItauTransactionValidation implements TransactionValidationStrategy{

    @Override
    public void validate(Wallet wallet, BigDecimal newamount) {
        if (newamount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("ITAU does not allow negative balance");
        }
    }

}
