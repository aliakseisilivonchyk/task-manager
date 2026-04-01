package com.github.aliakseisilivonchyk.taskmanager.dto;

import com.github.aliakseisilivonchyk.taskmanager.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на создание пользователя")
public record UserRequest(
        @Schema(description = "Логин", example = "admin") @NotBlank String username,
        @Schema(description = "Электронная почта", example = "admin@test.com") @Email String email,
        @Schema(description = "Пароль", example = "secret") @NotBlank String password,
        @Schema(description = "Роль", example = "ADMIN") UserRole role) {
}
