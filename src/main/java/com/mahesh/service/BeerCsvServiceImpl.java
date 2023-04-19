package com.mahesh.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mahesh.dto.BeerCSVRecord;
import com.opencsv.bean.CsvToBeanBuilder;

@Service
public class BeerCsvServiceImpl implements BeerCsvService {

	@Override
	public List<BeerCSVRecord> convertCSV(File csvFile) {
		try {
			List<BeerCSVRecord> beerCSVRecords = new CsvToBeanBuilder<BeerCSVRecord>(new FileReader(csvFile))
					.withType(BeerCSVRecord.class).build().parse();
			return beerCSVRecords;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
