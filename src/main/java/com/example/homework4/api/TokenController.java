package com.example.homework4.api;

import com.example.homework4.dto.JwtAuthenticationResponse;
import com.example.homework4.services.AuthenticationService;
import com.example.homework4.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/token")
@RequiredArgsConstructor
@Tag(name = "Управление refresh токеном")
public class TokenController {
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;

    @PostMapping("/refresh")
    @Operation(summary = "Обновление Access токена")
    public JwtAuthenticationResponse refresh(@RequestBody JwtAuthenticationResponse request) {
        return  authenticationService.refreshToken(request);
    }

    @PostMapping("/revoking")
    @Operation(summary = "Отзыв Refresh токена")
    public ResponseEntity<?> revoke(@RequestBody JwtAuthenticationResponse request) {
        return ResponseEntity.ok().body(tokenService.revokeRefreshToken(request.getRefreshToken()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleInvalidEnum(ResponseStatusException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token revoked");
    }
}
