package com.github.aliakseisilivonchyk.taskmanager.controller;

import com.github.aliakseisilivonchyk.taskmanager.dto.TaskRequest;
import com.github.aliakseisilivonchyk.taskmanager.dto.TaskResponse;
import com.github.aliakseisilivonchyk.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Задачи")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Создать задачу")
    public void create(@RequestBody TaskRequest taskRequest) {
        taskService.create(taskRequest);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Получить задачу по ID")
    public TaskResponse findById(@Schema(description = "ID задачи") @PathVariable Long id) {
        return taskService.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@taskService.isCurrentUserAuthor(#id) or hasRole('ADMIN')")
    @Operation(summary = "Обновить задачу")
    public void update(@Schema(description = "ID задачи") @PathVariable Long id, @RequestBody TaskRequest taskRequest) {
        taskService.update(id, taskRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@taskService.isCurrentUserAuthor(#id) or hasRole('ADMIN')")
    @Operation(summary = "Удалить задачу")
    public void delete(@Schema(description = "ID задачи") @PathVariable Long id) {
        taskService.delete(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Получить список задач (с фильтрами)")
    public List<TaskResponse> findAll(@Schema(description = "Статус") @Param("status") Optional<String> status,
                                      @Schema(description = "Исполнитель") @Param("assigneeId") Optional<Long> assigneeId,
                                      @Schema(description = "Автор задачи") @Param("authorId") Optional<Long> authorId) {
        return taskService.findAll(status, assigneeId, authorId);
    }
}
