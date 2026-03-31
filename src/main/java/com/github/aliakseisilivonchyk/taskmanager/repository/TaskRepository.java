package com.github.aliakseisilivonchyk.taskmanager.repository;

import com.github.aliakseisilivonchyk.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;

public interface TaskRepository extends ListCrudRepository<Task, Long>, JpaSpecificationExecutor<Task> {
}
