package com.kanban.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kanban.dto.TaskDTO;
import com.kanban.model.Task;
import com.kanban.model.TaskStatus;
import com.kanban.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

	private final TaskService taskService;

	@Autowired
	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}

	@GetMapping
	public ResponseEntity<List<Task>> getAllTasks() {
		return ResponseEntity.ok(taskService.getAllTasks());
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable TaskStatus status) {
		return ResponseEntity.ok(taskService.getTasksByStatus(status));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
		return ResponseEntity.ok(taskService.getTaskById(id));
	}

	@PostMapping
	public ResponseEntity<Task> createTask(@Valid @RequestBody TaskDTO taskDTO) {
		Task createdTask = taskService.createTask(taskDTO);
		return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {
		return ResponseEntity.ok(taskService.updateTask(id, taskDTO));
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id, @RequestBody Map<String, String> statusUpdate) {
		TaskStatus status = TaskStatus.valueOf(statusUpdate.get("status"));
		return ResponseEntity.ok(taskService.updateTaskStatus(id, status));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
		taskService.deleteTask(id);
		return ResponseEntity.noContent().build();
	}
}