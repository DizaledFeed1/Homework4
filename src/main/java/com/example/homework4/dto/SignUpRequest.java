package com.example.homework4.dto;

import com.example.homework4.dataBase.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;


@Data
public class SignUpRequest{

    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Логин не может быть пустым")
    private String username;

    @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
    @NotBlank(message = "Адрес электронной почты не может быть пустыми")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    private String email;

    @Size(min = 5, max = 255, message = "Длина пароля должна быть не более 255 символов")
    private String password;

    @NonNull
    private Role role;
}
