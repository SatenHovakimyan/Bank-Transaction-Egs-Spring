package com.egs.bank.transaction.spring.entity;

import com.egs.bank.transaction.spring.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "age")
    private int age;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "created_data")
    private LocalDate created_data;

    @OneToMany(mappedBy = "user")
    private Set<BankAccounts> bankAccounts;

    @OneToMany(mappedBy = "user")
    private List<Transactions> transactions;

    @Column(name = "logged_in_status")
    private Boolean loggedInStatus = false;

    public Users(String firstName, String lastName, Role role, String username, String password, int age,
                 LocalDate dateOfBirth, String email, LocalDate created_data, Set<BankAccounts> bankAccounts,
                 List<Transactions> transactions, Boolean loggedInStatus) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.username = username;
        this.password = password;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.created_data = created_data;
        this.bankAccounts = bankAccounts;
        this.transactions = transactions;
        this.loggedInStatus = loggedInStatus;
    }
}
