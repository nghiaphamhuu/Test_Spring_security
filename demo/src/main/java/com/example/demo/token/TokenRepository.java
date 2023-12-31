package com.example.demo.token;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, Integer> {

   @Query(value = """
       select t from Token t inner join User u\s
       on t.user.id = u.id\s
       where u.id = :id and (t.expiresIn < CURDATE())\s
       """)
   List<Token> findAllValidTokenByUser(Integer id);

    Optional<Token> findByToken(String token);

    @Query(value = """
       select t from Token t inner join User u\s
       on t.user.id = u.id\s
       where u.id = :id \s
       """)
    List<Token> findAllTokenByUser(Integer id);

  //@Query("DELETE FROM tokens WHERE userId = (SELECT id FROM users WHERE email = :email LIMIT 1) ")
  //void deleteTokenByEmail(String email);
}
