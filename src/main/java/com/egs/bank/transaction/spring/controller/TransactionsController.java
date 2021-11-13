package com.egs.bank.transaction.spring.controller;

import com.egs.bank.transaction.spring.entity.Transactions;
import com.egs.bank.transaction.spring.service.impl.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionsController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit/{user_id}")
    public ResponseEntity<Transactions> createDeposit(
            @PathVariable(name = "user_id") Long userId,
            @RequestParam(name = "bank_account_id") Long bankAccountId,
            @RequestParam(name = "transaction_amount") Long transactionAmount
            ) {
        Transactions transaction = transactionService.createDeposit(userId, bankAccountId, transactionAmount);
        if(transaction != null) {
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } else {
            return null;
        }
    }
    @PostMapping("/withdrawal/{user_id}")
    public ResponseEntity<Transactions> createWithdrawal(
            @PathVariable(name = "user_id") Long userId,
            @RequestParam(name = "bank_account_id") Long bankAccountId,
            @RequestParam(name = "transaction_amount") Long transactionAmount
            ) {
        Transactions transaction = transactionService.createWithdrawal(userId, bankAccountId, transactionAmount);
        if(transaction != null) {
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } else {
            return null;
        }
    }

    @GetMapping("/admin_transactions/{admin_id}")
    public List<Transactions> adminTransactions(
            @PathVariable(name = "admin_id") Long adminId
    ) {
        return transactionService.getTransactions(adminId);
    }

    @PostMapping("/acceptTransaction/{admin_id}")
    public ResponseEntity acceptTransaction(
            @PathVariable(name = "admin_id") Long adminId,
            @RequestParam(name = "transaction_id") UUID transactionId
            ) {
        transactionService.acceptTransaction(adminId, transactionId);
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }

}


