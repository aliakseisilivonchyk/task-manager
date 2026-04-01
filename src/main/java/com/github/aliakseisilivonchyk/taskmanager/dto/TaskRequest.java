package com.github.aliakseisilivonchyk.taskmanager.dto;

import com.github.aliakseisilivonchyk.taskmanager.model.TaskPriority;
import com.github.aliakseisilivonchyk.taskmanager.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Запрос на создание/обновление задачи")
public record TaskRequest(
        @Schema(description = "Название задачи", example = "testing") String title,
        @Schema(description = "Описание", example = "testing of a feature") String description,
        @Schema(description = "Статус", example = "TODO") TaskStatus status,
        @Schema(description = "Приоритет", example = "LOW") TaskPriority priority,
        @Schema(description = "Исполнитель", example = "1") Long assignee) {
}
