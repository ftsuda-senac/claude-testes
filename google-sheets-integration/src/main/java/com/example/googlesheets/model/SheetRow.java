package com.example.googlesheets.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SheetRow {

	private String rowId; // Used for identifying rows for updates

	private List<Object> values; // The actual values in the row
}
