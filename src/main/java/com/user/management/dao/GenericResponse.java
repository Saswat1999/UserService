package com.user.management.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GenericResponse {

	@JsonIgnore
	int statusCode;
	String message;
	Status status;

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public GenericResponse(int statusCode, String message, Status status) {
		this.statusCode = statusCode;
		this.message = message;
		this.status = status;
	}

	public GenericResponse() {

	}
}
