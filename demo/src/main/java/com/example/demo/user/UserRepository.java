package com.example.demo.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<UserDetails>  findByEmail(String email);

   @Query(value = """
     SELECT u FROM users u INNER JOIN tokens t\s
     on t.userId = u.id\s
     WHERE t.refreshToken = :refreshToken AND t.expiresIn < CURDATE() \s
     """)
   List<UserDetails> findUserByRefreshToken(@Param("refreshToken") String refreshToken);
} 
    

