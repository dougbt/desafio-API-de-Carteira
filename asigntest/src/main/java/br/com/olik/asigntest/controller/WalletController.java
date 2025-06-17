package br.com.olik.asigntest.controller;

import br.com.olik.asigntest.dto.TransactionDto;
import br.com.olik.asigntest.service.TransactionalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final TransactionalService transactionalService;

    @GetMapping("/amount")
    public ResponseEntity<BigDecimal> getAmount(@RequestParam Long userId) {
        BigDecimal value = transactionalService.getAmount(userId);

        return ResponseEntity.ok(value);
   }

    @PostMapping("/transaction")
    public ResponseEntity<BigDecimal> transaction(@RequestBody TransactionDto transactionDto) {
        log.info("Start Transaction {}", transactionDto);
        BigDecimal newAmount = transactionalService.processTransaction(transactionDto);

        return ResponseEntity.ok(newAmount);
    }

}
