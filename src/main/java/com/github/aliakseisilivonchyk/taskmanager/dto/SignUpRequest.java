package com.github.aliakseisilivonchyk.taskmanager.dto;

import com.github.aliakseisilivonchyk.taskmanager.model.UserRole;

public record SignUpRequest(String username, String email, String password, UserRole role) {
}
