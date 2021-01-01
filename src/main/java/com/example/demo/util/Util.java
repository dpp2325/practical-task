package com.example.demo.util;

import java.util.List;

import org.apache.commons.csv.CSVRecord;

import com.example.demo.common.FILE_HEADER;

public class Util {

	public static boolean isValidRecord(CSVRecord csvRecord) {
		if (null == csvRecord.get(FILE_HEADER.PRIMARY_KEY)) {
			return false;
		}
		return true;
	}

	public static boolean isValidHeaders(List<String> headers) {
		boolean isValid = true;
		int i = 0;
		if (headers.size() == FILE_HEADER.values().length) {
			for (String header : headers) {
				if (!header.equals(FILE_HEADER.values()[i++].toString())) {
					isValid = false;
				}
			}
		} else {
			isValid = false;
		}

		return isValid;
	}

}
