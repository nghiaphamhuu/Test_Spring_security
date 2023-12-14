package com.example.demo.token;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {
    @Id
    @GeneratedValue
    public Integer id;

    public String token;

    public String refreshToken;

   // @ManyToOne(fetch = FetchType.LAZY)
  //  @JoinTable(name = "users")
   // @JoinColumn(name = "userId")
    public String userId;

    public Date expiresIn;

    public Date updatedAt;

    public Date createdAt;
}
