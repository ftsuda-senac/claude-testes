package com.example.googlesheets.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.googlesheets.model.SheetRow;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;

@Service
public class GoogleSheetsService {

	private final Sheets sheetsService;

	@Value("${google.sheets.spreadsheetId}")
	private String spreadsheetId;

	@Value("${google.sheets.sheetName}")
	private String sheetName;

	@Autowired
	public GoogleSheetsService(Sheets sheetsService) {
		this.sheetsService = sheetsService;
	}

	/**
	 * Add a new row to the Google Sheet
	 */
	public void addRow(SheetRow row) throws IOException {
		// Get the current number of rows
		int lastRow = getLastRowNum() + 1;

		// Prepare the update values request
		ValueRange body = new ValueRange().setValues(Collections.singletonList(row.getValues()));

		// Execute the append request
		sheetsService.spreadsheets().values().append(spreadsheetId, sheetName + "!A" + lastRow, body)
				.setValueInputOption("USER_ENTERED").execute();
	}

	/**
	 * Update an existing row in the Google Sheet
	 */
	public void updateRow(SheetRow row) throws IOException {
		// Find the row to update
		int rowNum = findRowByIdColumn(row.getRowId());

		if (rowNum > 0) {
			// Prepare the update values request
			ValueRange body = new ValueRange().setValues(Collections.singletonList(row.getValues()));

			// Execute the update request
			sheetsService.spreadsheets().values().update(spreadsheetId, sheetName + "!A" + rowNum, body)
					.setValueInputOption("USER_ENTERED").execute();
		} else {
			throw new IOException("Row with ID " + row.getRowId() + " not found.");
		}
	}

	/**
	 * Get all rows from the Google Sheet
	 */
	public List<SheetRow> getAllRows() throws IOException {
		ValueRange response = sheetsService.spreadsheets().values().get(spreadsheetId, sheetName).execute();

		List<List<Object>> values = response.getValues();
		List<SheetRow> rows = new ArrayList<>();

		if (values != null && !values.isEmpty()) {
			// Assuming the first column contains the row ID
			for (List<Object> row : values) {
				if (!row.isEmpty()) {
					String rowId = row.get(0).toString();
					rows.add(new SheetRow(rowId, row));
				}
			}
		}

		return rows;
	}

	/**
	 * Get a specific row by its ID
	 */
	public SheetRow getRow(String rowId) throws IOException {
		int rowNum = findRowByIdColumn(rowId);

		if (rowNum > 0) {
			ValueRange response = sheetsService.spreadsheets().values()
					.get(spreadsheetId, sheetName + "!A" + rowNum + ":ZZ" + rowNum).execute();

			List<List<Object>> values = response.getValues();

			if (values != null && !values.isEmpty()) {
				List<Object> rowValues = values.get(0);
				return new SheetRow(rowId, rowValues);
			}
		}

		return null;
	}

	/**
	 * Get all sheet names in the spreadsheet
	 * 
	 * @return A list of all sheet names in the spreadsheet
	 * @throws IOException If an API error occurs
	 */
	public List<String> getSheetNames() throws IOException {
		// Get the spreadsheet metadata
		Spreadsheet spreadsheet = sheetsService.spreadsheets().get(spreadsheetId).execute();

		// Extract sheet names from the sheet metadata
		List<String> sheetNames = spreadsheet.getSheets().stream().map(sheet -> sheet.getProperties().getTitle())
				.toList();

		return sheetNames;
	}

	/**
	 * Helper method to find a row by ID (assuming ID is in the first column)
	 */
	private int findRowByIdColumn(String rowId) throws IOException {
		ValueRange response = sheetsService.spreadsheets().values().get(spreadsheetId, sheetName + "!A:A").execute();

		List<List<Object>> values = response.getValues();

		if (values != null) {
			for (int i = 0; i < values.size(); i++) {
				List<Object> row = values.get(i);
				if (!row.isEmpty() && row.get(0).toString().equals(rowId)) {
					return i + 1; // +1 because Google Sheets is 1-indexed
				}
			}
		}

		return -1; // Not found
	}

	/**
	 * Helper method to get the last row number in the sheet
	 */
	private int getLastRowNum() throws IOException {
		ValueRange response = sheetsService.spreadsheets().values().get(spreadsheetId, sheetName + "!A:A").execute();

		List<List<Object>> values = response.getValues();
		return values != null ? values.size() : 0;
	}

}
