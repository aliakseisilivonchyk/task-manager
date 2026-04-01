package com.github.aliakseisilivonchyk.taskmanager.service;

import com.github.aliakseisilivonchyk.taskmanager.dto.TaskRequest;
import com.github.aliakseisilivonchyk.taskmanager.dto.TaskResponse;
import com.github.aliakseisilivonchyk.taskmanager.model.Task;
import com.github.aliakseisilivonchyk.taskmanager.model.User;
import com.github.aliakseisilivonchyk.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;

    public TaskResponse create(TaskRequest taskRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User author = (User) userService.loadUserByUsername(userDetails.getUsername());
        User assignee = userService.findById(taskRequest.assignee()).get();

        Task task = convertToModel(taskRequest);
        task.setAuthor(author);
        task.setAssignee(assignee);

        return convertToDto(taskRepository.save(task));
    }

    public Optional<TaskResponse> findById(Long id) {
        return taskRepository.findById(id).map(TaskService::convertToDto);
    }

    public TaskResponse update(Long id, TaskRequest taskRequest) {
        Task task = taskRepository.findById(id).get();
        task.setTitle(taskRequest.title());
        task.setDescription(taskRequest.description());
        task.setStatus(taskRequest.status());
        task.setPriority(taskRequest.priority());

        User assignee = userService.findById(taskRequest.assignee()).get();
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
                    criteriaBuilder.equal(root.get("status"), status.get())
            );
        }

        if (assigneeId.isPresent()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("assignee").get("id"), assigneeId.get())
            );
        }

        if (authorId.isPresent()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("author").get("id"), authorId.get())
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
