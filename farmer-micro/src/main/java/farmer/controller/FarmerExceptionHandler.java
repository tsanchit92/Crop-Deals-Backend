/*
 * a package farmer.controller;
 * 
 * import java.time.LocalDateTime;
 * 
 * import org.springframework.http.ResponseEntity; import
 * org.springframework.web.bind.annotation.ControllerAdvice; import
 * org.springframework.web.bind.annotation.ExceptionHandler; import
 * org.springframework.web.context.request.WebRequest; import
 * org.springframework.web.servlet.mvc.method.annotation.
 * ResponseEntityExceptionHandler;
 * 
 * import farmer.exception.FarmerException; import
 * farmer.model.FarmerExceptionResponse; import lombok.extern.slf4j.Slf4j;
 * 
 * @ControllerAdvice
 * 
 * @Slf4j public class FarmerExceptionHandler extends
 * ResponseEntityExceptionHandler {
 * 
 * @ExceptionHandler(FarmerException.class) public ResponseEntity<Object>
 * handleException(FarmerException exception, WebRequest request) {
 * logger.info("invoked"); FarmerExceptionResponse response = new
 * FarmerExceptionResponse(); response.setDate(LocalDateTime.now());
 * response.setStatus(exception.getStatus());
 * response.setMessage(exception.getMessage()); ResponseEntity<Object> entity =
 * new ResponseEntity<Object>(response, exception.getStatus()); return entity;
 * 
 * }
 * 
 * }
 */