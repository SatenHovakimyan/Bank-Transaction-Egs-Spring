package com.egs.bank.transaction.spring.controller;

import com.egs.bank.transaction.spring.encrypt.PasswordEncryption;
import com.egs.bank.transaction.spring.entity.Users;
import com.egs.bank.transaction.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;


@RestController
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/welcome")
    public String HelloFromUsersController() {
        return "Hello bank user";
    }

    @PostMapping(value = "/register", consumes = "application/json")
    @ResponseBody
    public ResponseEntity register(
            @RequestBody
                    Users user) {
        if(userService.register(user) == 200) {
            return ResponseEntity.ok((HttpStatus.OK));
        } else {
            return  ResponseEntity.ok(HttpStatus.CONFLICT);
        }
    }
    @GetMapping(value = "/login", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Users login(
            @RequestBody
                    Users user) {

        return userService.login(user);
    }

}
