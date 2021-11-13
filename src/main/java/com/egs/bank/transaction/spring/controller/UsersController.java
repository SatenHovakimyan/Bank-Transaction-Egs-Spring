package com.egs.bank.transaction.spring.controller;

import com.egs.bank.transaction.spring.entity.Transactions;
import com.egs.bank.transaction.spring.entity.Users;
import com.egs.bank.transaction.spring.service.impl.TransactionService;
import com.egs.bank.transaction.spring.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
public class UsersController {
    private final UserService userService;
    private final TransactionService transactionService;

    @Autowired
    public UsersController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
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
        if (userService.register(user) == 200) {
            return ResponseEntity.ok((HttpStatus.OK));
        } else {
            return ResponseEntity.ok(HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/login", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity<Users> login(
            @RequestBody
                    Users user) {

        if (userService.login(user) != null) {
            return new ResponseEntity<>(userService.login(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<Users> getUserById (
            @PathVariable(name = "id") Long id
    ) {
        if(userService.getLoggedInUser(id) != null) {
            return new ResponseEntity<>(userService.getLoggedInUser(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

    }

    @GetMapping("/logout/{id}")
    @ResponseBody
    public ResponseEntity logout(
            @PathVariable Long id
    ) {
        userService.logout(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/change_role/{admin_id}")
    public ResponseEntity<Users> changeRole(
            @PathVariable(name = "admin_id") Long adminId,
            @RequestParam(name = "user_id") Long userId) {
        Users adminUser = userService.changeRole(adminId, userId);
        if (adminUser != null) {
            return new ResponseEntity<>(adminUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

}
