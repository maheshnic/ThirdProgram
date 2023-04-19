package com.mahesh.service;

import java.io.File;
import java.util.List;

import com.mahesh.dto.BeerCSVRecord;

public interface BeerCsvService {
	List<BeerCSVRecord> convertCSV(File csvFile);
}
