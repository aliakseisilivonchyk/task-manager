package com.github.aliakseisilivonchyk.taskmanager.controller;

import com.github.aliakseisilivonchyk.taskmanager.dto.TaskRequest;
import com.github.aliakseisilivonchyk.taskmanager.dto.TaskResponse;
import com.github.aliakseisilivonchyk.taskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void create(@RequestBody TaskRequest taskRequest) {
        taskService.create(taskRequest);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public TaskResponse findById(@PathVariable Long id) {
        return taskService.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@taskService.isCurrentUserAuthor(#id) or hasRole('ADMIN')")
    public void update(@PathVariable Long id, @RequestBody TaskRequest taskRequest) {
        taskService.update(id, taskRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@taskService.isCurrentUserAuthor(#id) or hasRole('ADMIN')")
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<TaskResponse> findAll(@Param("status") Optional<String> status,
                                      @Param("assigneeId") Optional<Long> assigneeId,
                                      @Param("authorId") Optional<Long> authorId) {
        return taskService.findAll(status, assigneeId, authorId);
    }
}
