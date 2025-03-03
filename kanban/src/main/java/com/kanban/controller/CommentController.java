package com.kanban.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kanban.dto.CommentDTO;
import com.kanban.model.Comment;
import com.kanban.service.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

	private final CommentService commentService;

	@Autowired
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@GetMapping("/task/{taskId}")
	public ResponseEntity<List<Comment>> getCommentsByTaskId(@PathVariable Long taskId) {
		return ResponseEntity.ok(commentService.getCommentsByTaskId(taskId));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
		return ResponseEntity.ok(commentService.getCommentById(id));
	}

	@PostMapping
	public ResponseEntity<Comment> createComment(@Valid @RequestBody CommentDTO commentDTO) {
		Comment createdComment = commentService.createComment(commentDTO);
		return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Comment> updateComment(@PathVariable Long id, @Valid @RequestBody CommentDTO commentDTO) {
		return ResponseEntity.ok(commentService.updateComment(id, commentDTO));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
		commentService.deleteComment(id);
		return ResponseEntity.noContent().build();
	}
}