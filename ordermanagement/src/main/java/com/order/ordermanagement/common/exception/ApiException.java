package com.order.ordermanagement.common.exception;

public class ApiException extends RuntimeException{
	
	private int httpStatus;
	private String errorCode;
	private String errorMessage;
	private String errorType;
	
	public ApiException(int httpStatus, String errorCode, String errorMessage, String errorType) {
		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.errorType = errorType;
	}
	
	public int getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorType() {
		return errorType;
	}
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
}