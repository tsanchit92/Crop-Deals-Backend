package farm.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import farm.exception.FarmException;
import farm.model.FarmExceptionResponse;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class FarmExceptionHandler extends ResponseEntityExceptionHandler{
	@ExceptionHandler(FarmException.class)
	public ResponseEntity<Object> handleException(FarmException exception,WebRequest request)
	{
		logger.info("invoked");
		FarmExceptionResponse response = new FarmExceptionResponse();
		response.setDate(LocalDateTime.now());
		response.setStatus(exception.getStatus());
		response.setMessage(exception.getMessage());
		ResponseEntity<Object> entity = new ResponseEntity<Object>(response, exception.getStatus());
		return entity;
		
	}
	
}