package com.kanban.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kanban.dto.TaskDTO;
import com.kanban.exception.ResourceNotFoundException;
import com.kanban.model.Task;
import com.kanban.model.TaskStatus;
import com.kanban.repository.TaskRepository;

@Service
public class TaskService {

	private final TaskRepository taskRepository;

	@Autowired
	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public List<Task> getAllTasks() {
		return taskRepository.findAll();
	}

	public List<Task> getTasksByStatus(TaskStatus status) {
		return taskRepository.findByStatus(status);
	}

	public Task getTaskById(Long id) {
		return taskRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
	}

	public Task createTask(TaskDTO taskDTO) {
		Task task = new Task();
		task.setTitle(taskDTO.getTitle());
		task.setDescription(taskDTO.getDescription());

		if (taskDTO.getStatus() != null) {
			task.setStatus(taskDTO.getStatus());
		} else {
			task.setStatus(TaskStatus.BACKLOG);
		}

		task.setCreatedAt(LocalDateTime.now());
		return taskRepository.save(task);
	}

	public Task updateTask(Long id, TaskDTO taskDTO) {
		Task existingTask = getTaskById(id);

		if (taskDTO.getTitle() != null) {
			existingTask.setTitle(taskDTO.getTitle());
		}

		if (taskDTO.getDescription() != null) {
			existingTask.setDescription(taskDTO.getDescription());
		}

		if (taskDTO.getStatus() != null) {
			existingTask.setStatus(taskDTO.getStatus());
		}

		existingTask.setUpdatedAt(LocalDateTime.now());
		return taskRepository.save(existingTask);
	}

	public Task updateTaskStatus(Long id, TaskStatus status) {
		Task task = getTaskById(id);
		task.setStatus(status);
		task.setUpdatedAt(LocalDateTime.now());
		return taskRepository.save(task);
	}

	public void deleteTask(Long id) {
		Task task = getTaskById(id);
		taskRepository.delete(task);
	}

}