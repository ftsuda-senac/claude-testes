package com.kanban.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kanban.dto.CommentDTO;
import com.kanban.exception.ResourceNotFoundException;
import com.kanban.model.Comment;
import com.kanban.model.Task;
import com.kanban.repository.CommentRepository;
import com.kanban.repository.TaskRepository;

@Service
public class CommentService {

	private final CommentRepository commentRepository;
	private final TaskRepository taskRepository;

	@Autowired
	public CommentService(CommentRepository commentRepository, TaskRepository taskRepository) {
		this.commentRepository = commentRepository;
		this.taskRepository = taskRepository;
	}

	public List<Comment> getCommentsByTaskId(Long taskId) {
		return commentRepository.findByTaskId(taskId);
	}

	public Comment getCommentById(Long id) {
		return commentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
	}

	public Comment createComment(CommentDTO commentDTO) {
		Task task = taskRepository.findById(commentDTO.getTaskId())
				.orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + commentDTO.getTaskId()));

		Comment comment = new Comment();
		comment.setContent(commentDTO.getContent());
		comment.setAuthor(commentDTO.getAuthor());
		comment.setTask(task);

		return commentRepository.save(comment);
	}

	public Comment updateComment(Long id, CommentDTO commentDTO) {
		Comment existingComment = getCommentById(id);

		if (commentDTO.getContent() != null) {
			existingComment.setContent(commentDTO.getContent());
		}

		if (commentDTO.getAuthor() != null) {
			existingComment.setAuthor(commentDTO.getAuthor());
		}

		return commentRepository.save(existingComment);
	}

	public void deleteComment(Long id) {
		Comment comment = getCommentById(id);
		commentRepository.delete(comment);
	}
}
