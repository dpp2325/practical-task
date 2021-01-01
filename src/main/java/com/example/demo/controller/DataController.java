package com.example.demo.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.common.Constants;
import com.example.demo.common.FILE_HEADER;
import com.example.demo.common.ResponseDTO;
import com.example.demo.dataobject.FileData;
import com.example.demo.service.DataStorageService;
import com.example.demo.util.Util;

@RestController
public class DataController {

	@Autowired
	public DataStorageService dataStorageService;

	private static final Logger logger = LoggerFactory.getLogger(DataController.class);

	@PostMapping("/uploadfile")
	public @ResponseBody ResponseDTO uploadFile(@RequestParam("file") MultipartFile file) {

		BufferedReader br;
		try {

			logger.info("Upload Data Service called: " + file.getName());
			InputStream is = file.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));

			CSVParser csvParser = new CSVParser(br, CSVFormat.EXCEL.withFirstRecordAsHeader());

			if (Util.isValidHeaders(csvParser.getHeaderNames())) {
				Iterable<CSVRecord> csvRecords = csvParser.getRecords();
				String errorMessage = saveCSVRecords(csvRecords);
				return ResponseDTO.ok(Constants.MSG_DATA_SAVED_SUCCESSFULLY, errorMessage);
			} else {
				return ResponseDTO.bad_request(Constants.MSG_INVALID_FILE_ERROR);
			}

		} catch (Exception e) {
			logger.error("ERROR", e);
			return ResponseDTO.server_error("Internal Server error.");
		}

	}

	@GetMapping("/data/{key}")
	public @ResponseBody ResponseDTO getData(@PathVariable("key") String key) {
		try {
			logger.info("Get Data Service called: " + key);
			FileData fileData = dataStorageService.getDataById(key);
			if (fileData != null) {
				return ResponseDTO.ok(dataStorageService.getDataById(key));
			} else {
				return ResponseDTO.not_found(Constants.MSG_ERROR_WHILE_GETTING_DATA);
			}

		} catch (Exception e) {
			logger.error("ERROR", e);
			return ResponseDTO.server_error(Constants.MSG_INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/data/{key}")
	public @ResponseBody ResponseDTO deleteData(@PathVariable("key") String key) {
		try {
			logger.info("Delete Data Service called: " + key);
			FileData fileData = dataStorageService.getDataById(key);
			if (fileData != null) {
				dataStorageService.deleteDataById(key);
				return ResponseDTO.ok(Constants.MSG_DATA_DELETED_SUCCESSFULLY);
			} else {
				return ResponseDTO.not_found(Constants.MSG_ERROR_WHILE_DELETING_DATA);
			}
			
		} catch (Exception e) {
			logger.error("ERROR", e);
			return ResponseDTO.server_error(Constants.MSG_INTERNAL_SERVER_ERROR);
		}
	}

	
	private static SimpleDateFormat simpleDateFormate = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
	
	private String saveCSVRecords(Iterable<CSVRecord> csvRecords) throws Exception {
		
		String errorMessage = "";
		List<FileData> fileDatas = new ArrayList<>();
		for (CSVRecord csvRecord : csvRecords) {
			if (Util.isValidRecord(csvRecord)) {
				try {
					FileData fileData = new FileData();
					fileData.setPrimaryKey(csvRecord.get(FILE_HEADER.PRIMARY_KEY.toString()));
					fileData.setName(csvRecord.get(FILE_HEADER.NAME.toString()));
					fileData.setDescription(csvRecord.get(FILE_HEADER.DESCRIPTION.toString()));
					fileData.setUpdatedTimestamp(
							simpleDateFormate.parse(csvRecord.get(FILE_HEADER.UPDATED_TIMESTAMP.toString())));
					fileDatas.add(fileData);
				} catch (Exception e) {
					errorMessage = errorMessage + "ERROR in saving record with primary key"
							+ csvRecord.get("PRIMARY_KEY") + ":" + e.getMessage() + '\n';
				}
			} else {
				errorMessage = errorMessage + "Invalid record for record number: " + csvRecord.getRecordNumber()
						+ '\n';
			}
		}

		dataStorageService.saveDataList(fileDatas);
		return errorMessage;
	}
}
