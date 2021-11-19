package com.egs.bank.transaction.system.controller;

import com.egs.bank.transaction.system.entity.BankAccounts;
import com.egs.bank.transaction.system.service.impl.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bank-accounts")
public class BankAccountsController {
    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountsController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/create/{admin_id}")
    @ResponseBody
    public ResponseEntity createBankAccount(
            @PathVariable(name = "admin_id") Long adminId,
            @RequestParam(name = "username") String username
    ) {
        BankAccounts bankAccount = bankAccountService.createBankAccount(adminId, username);
        if(bankAccount != null) {
            return  ResponseEntity.ok(HttpStatus.CREATED);
        } else {
            return ResponseEntity.ok(HttpStatus.CONFLICT);
        }
    }
}
