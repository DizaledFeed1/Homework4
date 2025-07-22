package com.example.homework4.dataBase;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private boolean revoked;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

