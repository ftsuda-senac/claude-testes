# Google Sheets Spring Boot Integration

This Spring Boot application allows you to interact with Google Sheets to:
- Add new data rows
- Update existing data rows
- Retrieve data (all rows or individual rows)

## Prerequisites

1. Java 17 or higher
2. Maven
3. Google Cloud account with the Google Sheets API enabled
4. Google API credentials

## Setup

### 1. Create Google Cloud Project and Enable Google Sheets API

1. Go to the [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Enable the Google Sheets API for your project
4. Create credentials (OAuth 2.0 Client ID) for a desktop application
5. Download the credentials JSON file

### 2. Configure the Application

1. Place the downloaded credentials JSON file in `src/main/resources/credentials.json`
2. Update `application.properties` with your spreadsheet details:
   ```properties
   google.sheets.spreadsheetId=YOUR_SPREADSHEET_ID
   google.sheets.sheetName=Sheet1
   ```

   To find your spreadsheet ID, look at the URL of your Google Sheet:
   ```
   https://docs.google.com/spreadsheets/d/SPREADSHEET_ID/edit
   ```

## Building and Running the Application

### Build
```bash
mvn clean package
```

### Run
```bash
java -jar target/google-sheets-integration-0.0.1-SNAPSHOT.jar
```

The first time you run the application, it will prompt you to authorize access to your Google Sheets. Follow the authorization steps in your browser.

## API Endpoints

### Add a New Row
```
POST /api/sheets/rows
```
Request Body:
```json
{
  "rowId": "unique_id",
  "values": ["unique_id", "value1", "value2", "value3"]
}
```

### Update an Existing Row
```
PUT /api/sheets/rows/{rowId}
```
Request Body:
```json
{
  "values": ["unique_id", "updated_value1", "updated_value2", "updated_value3"]
}
```

### Get All Rows
```
GET /api/sheets/rows
```

### Get a Specific Row
```
GET /api/sheets/rows/{rowId}
```

## Data Structure

The application assumes your Google Sheet has a structure where:
- The first column contains a unique identifier for each row
- The remaining columns contain the data you want to store

## Authentication Flow

The application uses OAuth 2.0 for authentication. On the first run, it will:
1. Open a browser window asking you to log in to your Google account
2. Request permission to access your Google Sheets
3. Store the authentication tokens in a local `tokens` directory

For subsequent runs, it will use the stored tokens without requiring re-authentication.
