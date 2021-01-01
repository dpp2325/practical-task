package com.example.demo.common;

import java.net.HttpURLConnection;
import java.util.Collection;

import org.springframework.validation.ObjectError;

public class ResponseDTO {
	private int httpStatus;
	private Object data;
	private String message;

	private ResponseDTO(int httpStatus, Object data, String message) {
		this.httpStatus = httpStatus;
		this.data = data;
		this.message = message;
	}
	
	private ResponseDTO(int httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	private ResponseDTO() {
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static ResponseDTO ok(Object body) {
		return new ResponseDTO(HttpURLConnection.HTTP_OK, body, null);
	}
	
	public static ResponseDTO ok(Object body,String message) {
		return new ResponseDTO(HttpURLConnection.HTTP_OK, body, message);
	}

	public static ResponseDTO not_found(String message) {
		return new ResponseDTO(HttpURLConnection.HTTP_NOT_FOUND, message);
	}

	public static ResponseDTO bad_request(String message) {
		return new ResponseDTO(HttpURLConnection.HTTP_BAD_REQUEST, null, message);
	}

	public static ResponseDTO server_error(String message) {
		return new ResponseDTO(HttpURLConnection.HTTP_INTERNAL_ERROR, message, "Server blew up");
	}

}
