package com.github.aliakseisilivonchyk.taskmanager.service;

import com.github.aliakseisilivonchyk.taskmanager.dto.TaskRequest;
import com.github.aliakseisilivonchyk.taskmanager.dto.TaskResponse;
import com.github.aliakseisilivonchyk.taskmanager.exception.ResourceNotFoundException;
import com.github.aliakseisilivonchyk.taskmanager.model.Task;
import com.github.aliakseisilivonchyk.taskmanager.model.User;
import com.github.aliakseisilivonchyk.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private static final String TASK_NOT_FOUND_MESSAGE = "Задача не найдена.";
    private static final String INCORRECT_ASSIGNEE_MESSAGE = "Исполнитель с таким ID не найден.";
    private static final String STATUS_ATTRIBUTE_NAME = "status";
    private static final String ASSIGNEE_ATTRIBUTE_NAME = "assignee";
    private static final String AUTHOR_ATTRIBUTE_NAME = "author";
    private static final String ID_ATTRIBUTE_NAME = "id";

    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskResponse create(TaskRequest taskRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User author = (User) userService.loadUserByUsername(userDetails.getUsername());
        User assignee = userService.findById(taskRequest.assignee())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, INCORRECT_ASSIGNEE_MESSAGE));

        Task task = convertToModel(taskRequest);
        task.setAuthor(author);
        task.setAssignee(assignee);

        return convertToDto(taskRepository.save(task));
    }

    public TaskResponse findById(Long id) {
        return taskRepository.findById(id).map(TaskService::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException(TASK_NOT_FOUND_MESSAGE));
    }

    public TaskResponse update(Long id, TaskRequest taskRequest) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(TASK_NOT_FOUND_MESSAGE));
        task.setTitle(taskRequest.title());
        task.setDescription(taskRequest.description());
        task.setStatus(taskRequest.status());
        task.setPriority(taskRequest.priority());

        User assignee = userService.findById(taskRequest.assignee())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, INCORRECT_ASSIGNEE_MESSAGE));
        task.setAssignee(assignee);

        return convertToDto(taskRepository.save(task));
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public List<TaskResponse> findAll(Optional<String> status, Optional<Long> assigneeId, Optional<Long> authorId) {
        Specification<Task> specification = Specification.unrestricted();

        if (status.isPresent()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(STATUS_ATTRIBUTE_NAME), status.get())
            );
        }

        if (assigneeId.isPresent()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(ASSIGNEE_ATTRIBUTE_NAME).get(ID_ATTRIBUTE_NAME), assigneeId.get())
            );
        }

        if (authorId.isPresent()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(AUTHOR_ATTRIBUTE_NAME).get(ID_ATTRIBUTE_NAME), authorId.get())
            );
        }

        return taskRepository.findAll(specification).stream()
                .map(TaskService::convertToDto)
                .toList();
    }

    public boolean isCurrentUserAuthor(Long taskId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return taskRepository.findById(taskId)
                .map(task -> task.getAuthor().getUsername().equals(userDetails.getUsername()))
                .orElse(false);
    }

    private static Task convertToModel(TaskRequest taskRequest) {
        Task task = new Task();
        task.setTitle(taskRequest.title());
        task.setDescription(taskRequest.description());
        task.setStatus(taskRequest.status());
        task.setPriority(taskRequest.priority());

        return task;
    }

    private static TaskResponse convertToDto(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(),
                task.getStatus(), task.getPriority(), task.getAuthor().getId(),
                task.getAssignee().getId(), task.getCreatedAt(), task.getUpdatedAt());
    }
}
