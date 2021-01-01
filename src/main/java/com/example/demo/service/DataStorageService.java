package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dataobject.FileData;
import com.example.demo.repository.DataRepository;

@Service
public class DataStorageService {
	
	@Autowired
	DataRepository dataRepository;
	
	public void saveDataList(List<FileData> dataList) throws Exception {
		dataRepository.saveAll(dataList);
	}
	
	public FileData getDataById(String key) throws Exception {
		return dataRepository.findById(key).get();
	}
	
	public void deleteDataById(String key) throws Exception {
		dataRepository.deleteById(key);
	}
	
}
