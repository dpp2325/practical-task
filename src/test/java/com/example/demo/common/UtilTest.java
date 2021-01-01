package com.example.demo.common;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.Test;

import com.example.demo.util.Util;

public class UtilTest {

	@Test
	public void givenValidRecordThenIsisValidRecordTrue() throws IOException {
		BufferedReader br;
		InputStream is = getClass().getClassLoader().getResourceAsStream("test_file.txt");
		br = new BufferedReader(new InputStreamReader(is));

		CSVParser csvParser = new CSVParser(br, CSVFormat.EXCEL.withFirstRecordAsHeader());
		assertEquals(true, Util.isValidHeaders(csvParser.getHeaderNames()));

	}

}
