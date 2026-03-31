package com.github.aliakseisilivonchyk.taskmanager.dto;

import com.github.aliakseisilivonchyk.taskmanager.model.TaskPriority;
import com.github.aliakseisilivonchyk.taskmanager.model.TaskStatus;

public record TaskRequest(String title, String description, TaskStatus status, TaskPriority priority,
                          Long assignee) {
}
