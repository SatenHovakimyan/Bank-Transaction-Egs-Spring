package com.egs.bank.transaction.system.service.impl;

import com.egs.bank.transaction.system.entity.BankAccounts;
import com.egs.bank.transaction.system.entity.Transactions;
import com.egs.bank.transaction.system.entity.Users;
import com.egs.bank.transaction.system.enums.Role;
import com.egs.bank.transaction.system.repository.BankAccountRepository;
import com.egs.bank.transaction.system.repository.TransactionRepository;
import com.egs.bank.transaction.system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public UserService(
            UserRepository userRepository, TransactionRepository transactionRepository, BankAccountRepository bankAccountRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Users getUserById(Long id) {
       return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }


    public int register(Users user) {
        Users registeredUser;
        registeredUser = user;
        if (user.getUsername().equals("admin")) {
            registeredUser.setRole(Role.ADMIN);
        } else {
            registeredUser.setRole(Role.USER);
        }
        registeredUser.setCreated_data(LocalDate.now());

        if (registeredUser.getDateOfBirth() != null) {
            registeredUser.setDateOfBirth(LocalDate.parse(user.getDateOfBirth().toString()));
        }

        registeredUser.setLoggedInStatus(false);
        String encodedPassword = Base64.getEncoder().encodeToString(user.getPassword().getBytes());
        registeredUser.setPassword(encodedPassword);

        if (userRepository.findByUsername(user.getUsername()) == null &&
                userRepository.findByEmail(user.getEmail()) == null) {
            userRepository.save(registeredUser);
            return 200;
        } else {
            return 409;
        }

    }

    public Users login(Users user) {
        String entirePassword = user.getPassword();
        String encryptedPassword = Base64.getEncoder().encodeToString(entirePassword.getBytes());
        Users loggedInUser = getUserByUsername(user.getUsername());
        if (
                (isRegisteredUser(userRepository.findByUsername(user.getUsername()).getId()) &&
                        encryptedPassword.equals(loggedInUser.getPassword()))) {
            loggedInUser.setLoggedInStatus(true);
            userRepository.save(loggedInUser);
            return userRepository.findByUsername(user.getUsername());
        }
        return null;
    }

    public void logout(Long id) {
        if (isLoggedIn(id)) {
            Users loggedOutUser = getUserById(id);
            loggedOutUser.setLoggedInStatus(false);
            userRepository.save(loggedOutUser);
        }
    }

    public boolean isRegisteredUser(Long id) {
        return userRepository.findById(id).isPresent();
    }

    public boolean isRegisteredUser(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public boolean isLoggedIn(Long id) {
        Users user = getUserById(id);
        if (isRegisteredUser(id)) {
            return user.getLoggedInStatus().equals(true);
        } else {
            return false;
        }
    }

    public Users getLoggedInUser(Long id) {
        if (isRegisteredUser(id) && isLoggedIn(id)) {
            return getUserById(id);
        } else {
            return null;
        }
    }

    public boolean hasAdminRole(Long id) {
        return getUserById(id).getRole().equals(Role.ADMIN);
    }

    public Users changeRole(Long adminId, Long userId) {
        Users changeableUser = getUserById(userId);
        Users adminUser = getUserById(adminId);
        Role roleToChange = changeableUser.getRole();
        if (adminUser != null &&
                adminUser.getRole().equals(Role.ADMIN) && isLoggedIn(adminId)) {
            if (roleToChange.equals(Role.ADMIN)) {
                changeableUser.setRole(Role.USER);
            } else {
                changeableUser.setRole(Role.ADMIN);
            }
            userRepository.save(changeableUser);
            return adminUser;
        }
        return null;
    }

    public Set<Transactions> getTransactionHistory(Long userId) {
        Set<Transactions> transactions = new HashSet<>();
        Users user = userRepository.findById(userId).orElse(null);
        if(bankAccountRepository.findByUser(userId) != null && user != null) {
            Set<BankAccounts> bankAccounts = bankAccountRepository.findByUser(userId);
            for (BankAccounts bankAccount:
                 bankAccounts) {
                Set<Transactions> transactionsSet = transactionRepository.findByBankAccount(bankAccount.getId());
                transactions.addAll(transactionsSet);
                }
            }
        transactions.forEach(transaction -> transaction.setBankAccount(null));
        return transactions;
    }
}
