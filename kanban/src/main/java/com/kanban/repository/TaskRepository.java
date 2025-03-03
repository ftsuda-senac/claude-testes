package com.kanban.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kanban.model.Task;
import com.kanban.model.TaskStatus;

public interface TaskRepository extends JpaRepository<Task, Long> {

	List<Task> findByStatus(TaskStatus status);
}