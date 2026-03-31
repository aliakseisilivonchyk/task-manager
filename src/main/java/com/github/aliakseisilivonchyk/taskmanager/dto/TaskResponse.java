package com.github.aliakseisilivonchyk.taskmanager.dto;

import com.github.aliakseisilivonchyk.taskmanager.model.TaskPriority;
import com.github.aliakseisilivonchyk.taskmanager.model.TaskStatus;

import java.time.ZonedDateTime;

public record TaskResponse(Long id, String title, String description, TaskStatus status, TaskPriority priority,
                           Long author, Long assignee, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
}
