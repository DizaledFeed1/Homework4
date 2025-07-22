package com.example.homework4.services;

import com.example.homework4.dataBase.RefreshToken;
import com.example.homework4.dataBase.TokenRepository;
import com.example.homework4.dataBase.User;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /**
     * Генерирует refresh-токен для пользователя, сохраняет его в базу данных.
     *
     * @param user пользователь, для которого создаётся токен
     * @param jwtService сервис для генерации JWT-токенов
     * @return строка с новым refresh-токеном
     */
    public String generateAndSaveRefreshToken(User user, JwtService jwtService) {
        String refreshToken = jwtService.generateRefreshToken(user);
        RefreshToken tokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiresAt(jwtService.extractExpiration(refreshToken))
                .revoked(false)
                .build();
        tokenRepository.save(tokenEntity);
        return refreshToken;
    }

    /**
     * Отзывает refresh-токен.
     * Устанавливает флаг revoked в значение true.
     *
     * @param token токен, который нужно отозвать
     * @return сообщение о результате отзыва
     * @throws IllegalArgumentException если токен не найден в базе данных
     */
    public String revokeRefreshToken(String token) {
        RefreshToken refreshToken = tokenRepository.findByToken(token);
        refreshToken.setRevoked(true);
        tokenRepository.save(refreshToken);
        return "Token revoked";
    }
}

