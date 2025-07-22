package com.example.homework4.dataBase;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByToken(String token);
}
