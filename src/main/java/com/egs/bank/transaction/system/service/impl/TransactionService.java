package com.egs.bank.transaction.system.service.impl;

import com.egs.bank.transaction.system.entity.Transactions;
import com.egs.bank.transaction.system.enums.TransactionStatus;
import com.egs.bank.transaction.system.enums.TransactionType;
import com.egs.bank.transaction.system.repository.BankAccountRepository;
import com.egs.bank.transaction.system.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {
    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountService bankAccountService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, UserService userService, BankAccountRepository bankAccountRepository, BankAccountService bankAccountService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountService = bankAccountService;
    }


    public boolean createDeposit(Long userId, Long bankAccountId, Long transactionAmount) {
        return createTransaction(userId, bankAccountId, transactionAmount, TransactionType.DEPOSIT);
    }


    public boolean createWithdrawal(Long userId, Long bankAccountId, Long transactionAmount) {
        return createTransaction(userId, bankAccountId, transactionAmount, TransactionType.WITHDRAWAL);
    }

    private boolean createTransaction(Long userId, Long bankAccountId, Long transactionAmount,
                                      TransactionType transactionType) {
        Transactions transaction = new Transactions();
        if (userService.isLoggedIn(userId) && bankAccountRepository.findById(bankAccountId).isPresent() &&
                transactionAmount >= 0L) {
            transaction.setCreatedData(LocalDateTime.now());
            transaction.setTransactionType(transactionType);
            transaction.setTransactionAmount(transactionAmount);
            transaction.setTransactionStatus(TransactionStatus.PENDING);
            transaction.setBankAccount(bankAccountRepository.findById(bankAccountId).orElseThrow(EntityNotFoundException::new));
            transactionRepository.save(transaction);
            return true;

        } else {
            return false;
        }
    }

    public List<Transactions> getTransactions(Long adminId) {
        if (userService.hasAdminRole(adminId)) {
            return transactionRepository.findAll();
        } else {
            return null;
        }
    }

    public boolean acceptOrRejectTransaction(Long adminId, UUID transactionId, String acceptOrRejectOrCancel) {
        if (userService.hasAdminRole(adminId) && transactionRepository.findById(transactionId).isPresent() &&
                transactionRepository.findById(transactionId).get().getTransactionStatus().equals(TransactionStatus.PENDING)) {
            Transactions transaction = transactionRepository.findById(transactionId).get();
            if (acceptOrRejectOrCancel.equals("accept")) {
                transaction.setTransactionStatus(TransactionStatus.ACCEPTED);
                if (transaction.getTransactionType().equals(TransactionType.DEPOSIT)) {
                    bankAccountService.depositBalance(transaction.getTransactionAmount(), transaction.getId());
                } else {
                    bankAccountService.withdrawalBalance(transaction.getTransactionAmount(), transaction.getId());
                }
            } else {
                transaction.setTransactionStatus(TransactionStatus.REJECTED);
            }
            transactionRepository.save(transaction);
            return true;
        } else {
            return false;
        }
    }

    public boolean rejectTransaction(Long adminId, UUID transactionId) {
        return (acceptOrRejectTransaction(adminId, transactionId, "reject"));
    }

    public boolean acceptTransaction(Long adminId, UUID transactionId) {
        return acceptOrRejectTransaction(adminId, transactionId, "accept");
    }


    public boolean cancelTransaction(Long userId, UUID transactionId) {
        Optional<Transactions> transactionO = transactionRepository.findById(transactionId);
        if(!userService.hasAdminRole(userId) && transactionO.isPresent()) {
           Transactions transaction = transactionO.get();
           if(transaction.getTransactionStatus().equals(TransactionStatus.PENDING) &&
                transaction.getBankAccount().getUser().getId().equals(userId)) {
               transaction.setTransactionStatus(TransactionStatus.CANCELED);
               transactionRepository.save(transaction);
               return true;
           } else {
               return false;
           }
        } else {
            return false;
        }
    }
}
