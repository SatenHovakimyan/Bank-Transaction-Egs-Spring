package com.egs.bank.transaction.spring.controller;

import com.egs.bank.transaction.spring.entity.Users;
import com.egs.bank.transaction.spring.service.impl.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank_accounts")
public class BankAccountsController {
    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountsController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/create_account{admin_id}")
    public ResponseEntity<Users> createBankAccount(
            @PathVariable(name = "admin_id") Long adminId,
            @RequestParam(name = "username") String username
    ) {
        if (bankAccountService.createBankAccount(adminId, username) != null) {
            return new ResponseEntity<>(
                    bankAccountService.createBankAccount(adminId, username),
                    HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }
}
