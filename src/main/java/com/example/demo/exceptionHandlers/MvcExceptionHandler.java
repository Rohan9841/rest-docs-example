package com.example.demo.exceptionHandlers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MvcExceptionHandler {

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<List<String>> handleConstraintException(ConstraintViolationException ex){
		List<String> errors = new ArrayList<>(ex.getConstraintViolations().size());
		ex.getConstraintViolations().forEach(v->{
			errors.add(v.toString());
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
}
