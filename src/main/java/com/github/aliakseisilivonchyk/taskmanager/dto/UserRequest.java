package com.github.aliakseisilivonchyk.taskmanager.dto;

import com.github.aliakseisilivonchyk.taskmanager.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на создание пользователя")
public record UserRequest(
        @Schema(description = "Логин", example = "admin") String username,
        @Schema(description = "Электронная почта", example = "admin@test.com") String email,
        @Schema(description = "Пароль", example = "secret") String password,
        @Schema(description = "Роль", example = "ADMIN") UserRole role) {
}
