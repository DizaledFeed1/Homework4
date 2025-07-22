package com.example.homework4.api;

import com.example.homework4.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
@RequiredArgsConstructor
@Tag(name = "Примеры",
description = "Примеры запросов с разными правами доступа")
public class ExampleController {

    @GetMapping
    @Operation(summary = "Доступен только авторизованным пользователям")
    public String example() {
        return "Hello, world!";
    }

    @GetMapping("/admin")
    @Operation(summary = "Доступен только авторизованным пользователям с ролью ADMIN")
    @PreAuthorize("hasRole('admin')")
    public String exampleAdmin() {
        return "Hello, admin!";
    }

    @GetMapping("/premium-user")
    @Operation(summary = "Доступен только авторизованным пользователям с ролью premium_user")
    @PreAuthorize("hasRole('premium_user')")
    public String examplePremiumUser() {
        return "Hello, premium user!";
    }

    @GetMapping("/guest")
    @Operation(summary = "Доступен только авторизованным пользователям с ролью guest")
    @PreAuthorize("hasRole('guest')")
    public String exampleGuest() {
        return "Hello, guest!";
    }
}
