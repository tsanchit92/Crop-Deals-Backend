package com.cotroller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.exception.LoginException;
import com.model.LoginExceptionResponse;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class LoginExceptionHandler extends ResponseEntityExceptionHandler{
	@ExceptionHandler(LoginException.class)
	public ResponseEntity<Object> handleException(LoginException exception,WebRequest request)
	{
		logger.info("invoked");
		LoginExceptionResponse response = new LoginExceptionResponse();
		response.setDate(LocalDateTime.now());
		response.setStatus(exception.getStatus());
		response.setMessage(exception.getMessage());
		ResponseEntity<Object> entity = new ResponseEntity<Object>(response, exception.getStatus());
		return entity;
		
	}
	
}
