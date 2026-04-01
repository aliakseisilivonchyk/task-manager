package com.github.aliakseisilivonchyk.taskmanager.dto;

import com.github.aliakseisilivonchyk.taskmanager.model.UserRole;

public record UserResponse(Long id, String username, String email, UserRole role) {
}
