package com.github.aliakseisilivonchyk.taskmanager.dto;

import com.github.aliakseisilivonchyk.taskmanager.model.TaskPriority;
import com.github.aliakseisilivonchyk.taskmanager.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.ZonedDateTime;

@Schema(description = "Задача")
public record TaskResponse(
        @Schema(description = "Уникальный идентификатор", example = "1") Long id,
        @Schema(description = "Название задачи", example = "testing") String title,
        @Schema(description = "Описание", example = "testing of a feature") String description,
        @Schema(description = "Статус", example = "TODO") TaskStatus status,
        @Schema(description = "Приоритет", example = "LOW") TaskPriority priority,
        @Schema(description = "Автор задачи", example = "1") Long author,
        @Schema(description = "Исполнитель", example = "1") Long assignee,
        @Schema(description = "Дата создания", example = "2026-04-01T07:14:09.160757Z") ZonedDateTime createdAt,
        @Schema(description = "Дата обновления", example = "2026-04-01T07:14:09.162211Z") ZonedDateTime updatedAt) {
}
