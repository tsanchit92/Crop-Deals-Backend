package admin.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import admin.exception.AdminException;
import admin.model.AdminExceptionResponse;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class AdminExceptionHandler extends ResponseEntityExceptionHandler{
	@ExceptionHandler(AdminException.class)
	public ResponseEntity<Object> handleException(AdminException exception,WebRequest request)
	{
		logger.info("invoked");
		AdminExceptionResponse response = new AdminExceptionResponse();
		response.setDate(LocalDateTime.now());
		response.setStatus(exception.getStatus());
		response.setMessage(exception.getMessage());
		ResponseEntity<Object> entity = new ResponseEntity<Object>(response, exception.getStatus());
		return entity;
		
	}
	
}
