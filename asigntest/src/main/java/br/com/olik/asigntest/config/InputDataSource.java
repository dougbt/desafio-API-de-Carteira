package br.com.olik.asigntest.config;

import br.com.olik.asigntest.entity.Wallet;
import br.com.olik.asigntest.repository.WalletRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InputDataSource {

    private final WalletRepository walletRepository;

    @PostConstruct
    public void init() {
        List<Wallet> wallets = List.of(
                buildWallet(1L, BigDecimal.valueOf(7), Wallet.WalletType.SANTANDER),
                buildWallet(2L, BigDecimal.valueOf(10), Wallet.WalletType.ITAU)
        );
        walletRepository.saveAll(wallets);
    }

    private Wallet buildWallet(Long userId, BigDecimal amount, Wallet.WalletType type) {
        return Wallet.builder()
                .amount(amount)
                .userId(userId)
                .type(type)
                .build();
    }
}
