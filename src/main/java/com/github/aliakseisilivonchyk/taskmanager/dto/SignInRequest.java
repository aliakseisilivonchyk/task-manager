package com.github.aliakseisilivonchyk.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на аутентификацию")
public record SignInRequest(
        @Schema(description = "Логин", example = "admin") @NotBlank String username,
        @Schema(description = "Пароль", example = "secret") @NotBlank String password) {
}
