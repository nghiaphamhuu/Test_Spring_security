package com.example.demo.token;

import java.util.Date;

import com.example.demo.user.User;

import jakarta.persistence.Column;
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

    public Date expiresIn;

    public Date updatedAt;

    public Date createdAt;

    // Use @JoinColumn to specify the foreign key column
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Use @Column(insertable = false, updatable = false) to indicate that this property is not insertable or updatable
    @Column(name = "user_id", insertable = false, updatable = false)
    private Integer userId;
}
