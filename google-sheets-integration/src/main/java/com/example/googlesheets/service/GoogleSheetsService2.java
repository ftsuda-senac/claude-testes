package com.example.googlesheets.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
// import com.google.auth.http.HttpCredentialsAdapter;
// import com.google.auth.oauth2.GoogleCredentials;

// Versão gerada pelo MS Copilot
@Service
public class GoogleSheetsService2 {

	private static final String APPLICATION_NAME = "Google Sheets Integration";
	private static final String SPREADSHEET_ID = "your-spreadsheet-id";
	private static Sheets sheetsService;

	public GoogleSheetsService2() throws GeneralSecurityException, IOException {
		// sheetsService = getSheetsService();
	}

	private Sheets getSheetsService() throws GeneralSecurityException, IOException {
		
//		GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("/path/to/credentials.json"))
//				.createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
//		return new Sheets.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(),
//				new HttpCredentialsAdapter(credentials)).setApplicationName(APPLICATION_NAME).build();

return null;
	}

	public void addRow(String sheetName, List<Object> rowData) throws IOException {
		ValueRange appendBody = new ValueRange().setValues(Collections.singletonList(rowData));
		sheetsService.spreadsheets().values().append(SPREADSHEET_ID, sheetName, appendBody).setValueInputOption("RAW")
				.execute();
	}

	public void updateRow(String sheetName, String range, List<Object> rowData) throws IOException {
		ValueRange body = new ValueRange().setValues(Collections.singletonList(rowData));
		sheetsService.spreadsheets().values().update(SPREADSHEET_ID, range, body).setValueInputOption("RAW").execute();
	}

	public List<List<Object>> getData(String sheetName, String range) throws IOException {
		ValueRange response = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, sheetName + "!" + range)
				.execute();
		return response.getValues();
	}
}
