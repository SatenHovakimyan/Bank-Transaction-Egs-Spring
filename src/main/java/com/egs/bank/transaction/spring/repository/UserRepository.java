package com.egs.bank.transaction.spring.repository;

import com.egs.bank.transaction.spring.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    @Query("SELECT user FROM Users user WHERE user.firstName = :firstName")
    List<Users> findByFirstName(@Param("firstName") String firstName);

    List<Users> findByLastName(String lastName);

    @Query("SELECT user FROM Users user WHERE user.username = :username")
    Users findByUsername(String username);

    @Query("SELECT user FROM Users user WHERE user.email = :email")
    Users findByEmail(String email);

    Optional<Users> findByID(Long id);

}