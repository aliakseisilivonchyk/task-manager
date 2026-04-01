package com.github.aliakseisilivonchyk.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT токен")
public record JwtAuthResponse(
        @Schema(description = "Значение токена", example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJub25lIn0.eyJ1c2VybmFtZSI6ImFsZWtzZWkiLCJyb2xlIjoiUk9MRV9BRE1JTiIsImV4cGlyYXRpb25UaW1lIjoxNzc1MDc1NDU2NjYxfQ.") String token) {
}
