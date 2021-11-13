package com.egs.bank.transaction.spring.service.impl;

import com.egs.bank.transaction.spring.entity.Transactions;
import com.egs.bank.transaction.spring.entity.Users;
import com.egs.bank.transaction.spring.enums.Role;
import com.egs.bank.transaction.spring.repository.TransactionRepository;
import com.egs.bank.transaction.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public UserService(
            UserRepository userRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Users getUserById(Long id) {
        return userRepository.findById(id).get();
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
        System.out.println(encodedPassword);
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
        Users loggedInUser = getUserByUsername(user.getUsername());
        System.out.println(loggedInUser.getPassword());
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
        return userRepository.findById(id).get() != null;
    }

    public boolean isRegisteredUser(String username) {
        return userRepository.findByUsername(username) != null;
    }

    public boolean isLoggedIn(Long id) {
        Users user = getUserById(id);
        if(isRegisteredUser(id)) {
            return user.getLoggedInStatus().equals(true);
        } else {
            return false;
        }
    }

    public Users getLoggedInUser(Long id) {
        if(isRegisteredUser(id) && isLoggedIn(id)) {
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
