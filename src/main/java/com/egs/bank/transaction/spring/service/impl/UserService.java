package com.egs.bank.transaction.spring.service.impl;

import com.egs.bank.transaction.spring.encrypt.PasswordEncryption;
import com.egs.bank.transaction.spring.entity.Users;
import com.egs.bank.transaction.spring.enums.Role;
import com.egs.bank.transaction.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Map<String, IvParameterSpec> encryptIvAttribute = new HashMap<>();
    private Map<String, SecretKey> encryptKeyAttribute = new HashMap<>();


    public int register(Users user) {
        Users registeredUser = new Users();
        registeredUser = user;
        if (user.getUsername().equals("admin")) {
            registeredUser.setRole(Role.ADMIN);
        } else {
            registeredUser.setRole(Role.USER);
        }
        registeredUser.setCreated_data(LocalDate.now());

        IvParameterSpec iv = PasswordEncryption.generateIv();
        SecretKey key1 = null;
        try {
            key1 = PasswordEncryption.generateKey(128);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            registeredUser.setPassword(PasswordEncryption.encrypt(user.getPassword(), key1, iv));
        } catch (NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException |
                IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if(registeredUser.getDateOfBirth() != null) {
            registeredUser.setDateOfBirth(LocalDate.parse(user.getDateOfBirth().toString()));
        }

        if (userRepository.findByUsername(user.getUsername()) == null &&
                userRepository.findByEmail(user.getEmail()) == null) {
            userRepository.save(registeredUser);
            encryptIvAttribute.put(registeredUser.getUsername(), iv);
            encryptKeyAttribute.put(registeredUser.getUsername(), key1);
            return 200;
        } else {
            return 409;
        }

    }

    public Users login(Users user) {
        String password = user.getPassword();
        SecretKey key1 = encryptKeyAttribute.get(user.getUsername());
        IvParameterSpec iv = encryptIvAttribute.get(user.getUsername());
        String decryptedPassword = null;
        String encryptedPassword = userRepository.findByUsername(user.getUsername()).getPassword();
        try {
            decryptedPassword = PasswordEncryption.decrypt(encryptedPassword, key1, iv);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (
                (userRepository.findByUsername(user.getUsername())) != null &&
                        password.equals(decryptedPassword)) {
            return userRepository.findByUsername(user.getUsername());
        }
        return null;
    }




}
