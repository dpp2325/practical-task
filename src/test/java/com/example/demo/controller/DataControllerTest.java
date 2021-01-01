package com.example.demo.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.common.Constants;
import com.example.demo.dataobject.FileData;
import com.example.demo.service.DataStorageService;

@WebMvcTest(DataController.class)
public class DataControllerTest {

	@Autowired
    private MockMvc mvc;

    @MockBean
    private DataStorageService service;
    
    private static String TEST_FILE_NAME = "test_file.txt";
    private static String PRIMARY_KEY = "PRIMARY_KEY";
    
    @Test
    public void givenValidFileThenUploadFileSuccess() throws Exception {
    	
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(TEST_FILE_NAME);
		File file = new File(getClass().getClassLoader().getResource(TEST_FILE_NAME).toURI());

		long byteLength = file.length(); // byte count of the file-content

		byte[] filecontent = new byte[(int) byteLength];
		inputStream.read(filecontent, 0, (int) byteLength);
		
		MockMultipartFile multipartFile = new MockMultipartFile("file",TEST_FILE_NAME, "text/plain", filecontent);
		mvc.perform(MockMvcRequestBuilders.multipart("/uploadfile")
               .file(multipartFile)
               .characterEncoding("UTF-8"))
               .andExpect(MockMvcResultMatchers.status().isOk());
	}
    
    @Test
    public void givenValidKeyThenDetDataReturnData() throws Exception {
    	
    	FileData fileData = createDummyFileData();
    	Mockito.when(service.getDataById(PRIMARY_KEY)).thenReturn(fileData);
    	
    	mvc.perform(get("/data/"+PRIMARY_KEY)
    		      .contentType(MediaType.TEXT_PLAIN))
    		      .andExpect(status().isOk())
    		      .andExpect(content()
    		      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
    		      .andExpect(jsonPath("$.data.name", is("name")));
    }
    
    @Test
    public void givenValidKeyThenDeleteDataReturnData() throws Exception {
    	
    	FileData fileData = createDummyFileData();
    	Mockito.when(service.getDataById(PRIMARY_KEY)).thenReturn(fileData);
    	
    	mvc.perform(delete("/data/"+PRIMARY_KEY)
    		      .contentType(MediaType.TEXT_PLAIN))
    		      .andExpect(status().isOk())
    		      .andExpect(content()
    		      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
    		      .andExpect(jsonPath("$.data", is(Constants.MSG_DATA_DELETED_SUCCESSFULLY)));
    }
    
    private static FileData createDummyFileData() {
    	FileData fileData = new FileData();
    	fileData.setPrimaryKey(PRIMARY_KEY);
    	fileData.setDescription("description");
    	fileData.setName("name");
    	return fileData;
    }
    
}
