package com.bharath.checkout.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CheckOutServiceExceptionHandler {

	@ExceptionHandler(CheckOutServiceException.class)
	public ResponseEntity<ErrorInformation> handleUserServiceException(CheckOutServiceException exception) {

		ErrorInformation errorInformation = new ErrorInformation();
		errorInformation.setErrorMessage(exception.getMessage());
		errorInformation.setOccuredAt(LocalDateTime.now());
		errorInformation.setErrorCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<ErrorInformation>(errorInformation, HttpStatus.BAD_REQUEST);
	}

}
