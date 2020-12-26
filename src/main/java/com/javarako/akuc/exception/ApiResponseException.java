package com.javarako.akuc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ApiResponseException extends RuntimeException {
	
	private static final long serialVersionUID = -4153182065211853683L;
	private String error;
    private HttpStatus httpStatus;

    public ApiResponseException(String error, HttpStatus errorCode){
    	super(error);
        this.error = error;
    	this.httpStatus = errorCode;
    }

}
