package com.example.homework4.services;

import com.example.homework4.dataBase.RefreshToken;
import com.example.homework4.dataBase.TokenRepository;
import com.example.homework4.dto.JwtAuthenticationResponse;
import com.example.homework4.dto.SignInRequest;
import com.example.homework4.dto.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.example.homework4.dataBase.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;


    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userService.create(user);

        var accesJwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        tokenService.generateAndSaveRefreshToken(user, jwtService);
        return new JwtAuthenticationResponse(accesJwt, refreshToken);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        var accesJwt = jwtService.generateToken(user);
        var refreshJwt = jwtService.generateRefreshToken(user);
        User userEntity = userService.getByUsername(user.getUsername());
        tokenService.generateAndSaveRefreshToken(userEntity, jwtService);
        return new JwtAuthenticationResponse(accesJwt, refreshJwt);
    }

    public JwtAuthenticationResponse refreshToken(JwtAuthenticationResponse request) {
        String refreshToken = request.getRefreshToken();
        RefreshToken refreshTokenEntity = tokenRepository.findByToken(refreshToken);

        if (jwtService.isRefreshTokenValid(refreshToken) && !refreshTokenEntity.isRevoked()) {
            String username = jwtService.extractUserName(refreshToken);
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(username);
            String newAccessToken = jwtService.generateToken(userDetails);
            return new JwtAuthenticationResponse(newAccessToken, refreshToken);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token revoked");
        }
    }
}
