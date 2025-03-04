package com.kanban.dto;

import com.kanban.model.TaskStatus;

import jakarta.validation.constraints.NotBlank;

public class TaskDTO {

	private Long id;

	@NotBlank(message = "Title is required")
	private String title;

	private String description;

	private TaskStatus status;

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}
}