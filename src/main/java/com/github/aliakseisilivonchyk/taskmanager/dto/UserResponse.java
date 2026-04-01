package com.github.aliakseisilivonchyk.taskmanager.dto;

import com.github.aliakseisilivonchyk.taskmanager.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Пользователь")
public record UserResponse(
        @Schema(description = "Уникальный идентификатор", example = "1") Long id,
        @Schema(description = "Логин", example = "admin") String username,
        @Schema(description = "Электронная почта", example = "admin@test.com") String email,
        @Schema(description = "Роль", example = "ADMIN") UserRole role) {
}
