package com.egs.bank.transaction.spring.service.impl;

import com.egs.bank.transaction.spring.entity.Users;
import com.egs.bank.transaction.spring.enums.Role;
import com.egs.bank.transaction.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Base64;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;

    @Autowired
    public UserService(
            UserRepository userRepository,
            BankAccountService bankAccountService,
            TransactionService transactionService) {
        this.userRepository = userRepository;
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
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
        System.out.println(encryptedPassword);
        System.out.println(userRepository.findByUsername(user.getUsername()).getPassword());
        if (
                (userRepository.findByUsername(user.getUsername())) != null &&
                        encryptedPassword.equals(userRepository.findByUsername(user.getUsername()).getPassword())) {
            userRepository.findByUsername((user.getUsername())).setLoggedInStatus(true);
            return userRepository.findByUsername(user.getUsername());
        }
        return null;
    }

    public void logout(Long id) {
        if (isLoggedIn(id)) {
            userRepository.getById(id).setLoggedInStatus(false);
        }
    }

    public boolean isLoggedIn(Long id) {
        return userRepository.getById(id).getLoggedInStatus().equals(true);
    }

    public boolean hasAdminRole(Long id) {
        return userRepository.getById(id).getRole().equals(Role.ADMIN);
    }

    public boolean isRegisteredUser(Long id) {
        return userRepository.getById(id) != null;
    }

    public boolean isRegisteredUser(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Users getUserById(Long id) {
        return userRepository.getById(id);
    }

    public Users changeRole(Long adminId, Long userId) {
        Users changeableUser = userRepository.getById(userId);
        Users adminUser = userRepository.getById(adminId);
        Role roleToChange = changeableUser.getRole();
        if (changeableUser != null && adminUser != null &&
                adminUser.getRole().equals(Role.ADMIN) && isLoggedIn(adminId)) {
                if(roleToChange.equals(Role.ADMIN)) {
                    changeableUser.setRole(Role.USER);
                } else {
                    changeableUser.setRole(Role.ADMIN);
                }
            userRepository.save(changeableUser);
                return adminUser;
        }
        return null;
    }
}
