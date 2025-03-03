package com.example.googlesheets.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.googlesheets.model.SheetRow;
import com.example.googlesheets.service.GoogleSheetsService;

@RestController
@RequestMapping("/api/sheets")
public class GoogleSheetsController {

	private final GoogleSheetsService googleSheetsService;

	@Autowired
	public GoogleSheetsController(GoogleSheetsService googleSheetsService) {
		this.googleSheetsService = googleSheetsService;
	}

	@PostMapping("/rows")
	public ResponseEntity<String> addRow(@RequestBody SheetRow row) {
		try {
			googleSheetsService.addRow(row);
			return new ResponseEntity<>("Row added successfully", HttpStatus.CREATED);
		} catch (IOException e) {
			return new ResponseEntity<>("Error adding row: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/rows/{rowId}")
	public ResponseEntity<String> updateRow(@PathVariable String rowId, @RequestBody SheetRow row) {
		try {
			// Ensure the rowId in the path matches the one in the body
			row.setRowId(rowId);
			googleSheetsService.updateRow(row);
			return new ResponseEntity<>("Row updated successfully", HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>("Error updating row: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/rows")
	public ResponseEntity<List<SheetRow>> getAllRows() {
		try {
			List<SheetRow> rows = googleSheetsService.getAllRows();
			return new ResponseEntity<>(rows, HttpStatus.OK);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/rows/{rowId}")
	public ResponseEntity<SheetRow> getRow(@PathVariable String rowId) {
		try {
			SheetRow row = googleSheetsService.getRow(rowId);
			if (row != null) {
				return new ResponseEntity<>(row, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
