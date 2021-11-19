package com.egs.bank.transaction.system.controller;

import com.egs.bank.transaction.system.entity.Transactions;
import com.egs.bank.transaction.system.service.impl.TransactionService;
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
    public ResponseEntity<String> createDeposit(
            @PathVariable(name = "user_id") Long userId,
            @RequestParam(name = "bank_account_id") Long bankAccountId,
            @RequestParam(name = "transaction_amount") Long transactionAmount
            ) {

        if(transactionService.createDeposit(userId, bankAccountId, transactionAmount)) {
            return new ResponseEntity<>("Created transaction with status PENDING", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Can't create transaction", HttpStatus.NOT_ACCEPTABLE);
        }
    }
    @PostMapping("/withdrawal/{user_id}")
    public ResponseEntity<String> createWithdrawal(
            @PathVariable(name = "user_id") Long userId,
            @RequestParam(name = "bank_account_id") Long bankAccountId,
            @RequestParam(name = "transaction_amount") Long transactionAmount
            ) {
        if(transactionService.createWithdrawal(userId, bankAccountId, transactionAmount)) {
            return new ResponseEntity<>("Created transaction with status PENDING", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Can't create transaction", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping("/admin-transactions-history/{admin_id}")
    public ResponseEntity<List<Transactions>> adminTransactions(
            @PathVariable(name = "admin_id") Long adminId
    ) {
        if(transactionService.getTransactions(adminId) != null) {
            return new ResponseEntity<>(transactionService.getTransactions(adminId), HttpStatus.OK);
        } else return new ResponseEntity<>(null, HttpStatus.CONFLICT);
    }

    @PostMapping("/accept/{admin-id}")
    public ResponseEntity<String> acceptTransaction(
            @PathVariable(name = "admin-id") Long adminId,
            @RequestParam(name = "transaction-id") UUID transactionId
            ) {
        if(transactionService.acceptTransaction(adminId, transactionId)) {
            return new ResponseEntity<>("Transaction has been accepted", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Can't accept transaction", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/reject/{admin-id}")
    public ResponseEntity<String> rejectTransaction(
            @PathVariable(name = "admin-id") Long adminId,
            @RequestParam(name = "transaction-id") UUID transactionId
    ) {
        if(transactionService.rejectTransaction(adminId, transactionId)) {
            return new ResponseEntity<>("Transaction has been rejected", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Can't reject transaction", HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/cancel/{user-id}")
    public ResponseEntity<String> cancelTransaction(
            @PathVariable(name = "user-id") Long userId,
            @RequestParam(name = "transaction-id") UUID transactionId
    ) {
        if(transactionService.cancelTransaction(userId, transactionId)) {
            return new ResponseEntity<>("Transaction has been canceled", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Can't cancel transaction", HttpStatus.NOT_ACCEPTABLE);
        }
    }

}


