package com.example.demo.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User>  findByEmail(String email);

  // @Query("SELECT u FROM users u INNER JOIN tokens t on t.userId = u.id WHERE t.refreshToken = :refreshToken AND t.expiresIn < CURDATE() ")
   //List<UserDetails> findByRefreshToken(@Param("refreshToken") String refreshToken);
} 
    

