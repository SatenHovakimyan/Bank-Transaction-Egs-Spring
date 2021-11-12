package com.egs.bank.transaction.spring.controller;

import com.egs.bank.transaction.spring.entity.BankAccounts;
import com.egs.bank.transaction.spring.service.impl.BankAccountService;
import com.egs.bank.transaction.spring.service.impl.TransactionService;
import com.egs.bank.transaction.spring.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionsController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final BankAccountService bankAccountService;

    @Autowired
    public TransactionsController(TransactionService transactionService, UserService userService, BankAccountService bankAccountService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }


}
