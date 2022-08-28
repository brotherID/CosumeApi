package dev.procheck.capmobile.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	public RestResponseEntityExceptionHandler() {
		super();
	}

	@ExceptionHandler({ FunctinalException.class })
	public ResponseEntity<Object> handleBadRequest(final FunctinalException functinalException, final WebRequest request) {
		ExceptionDetail exceptionDetail = new ExceptionDetail();
		exceptionDetail.setMessage(functinalException.getMessage());
		return new ResponseEntity<>(exceptionDetail, new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}

}