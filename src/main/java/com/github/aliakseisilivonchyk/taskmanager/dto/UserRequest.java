package com.github.aliakseisilivonchyk.taskmanager.dto;

import com.github.aliakseisilivonchyk.taskmanager.model.UserRole;

public record UserRequest(String username, String email, String password, UserRole role) {
}
