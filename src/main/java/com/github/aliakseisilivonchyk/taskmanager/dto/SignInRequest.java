package com.github.aliakseisilivonchyk.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на аутентификацию")
public record SignInRequest(
        @Schema(description = "Логин", example = "admin") String username,
        @Schema(description = "Пароль", example = "secret") String password) {
}
