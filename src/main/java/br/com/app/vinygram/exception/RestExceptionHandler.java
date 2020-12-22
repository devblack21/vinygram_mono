package br.com.app.vinygram.exception;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.omg.CORBA.portable.UnknownException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javassist.NotFoundException;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler{

	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		Map<String,String> responseBody = new HashMap<>();
        responseBody.put("path",request.getContextPath());
        responseBody.put("message","The URL you have reached is not in service at this time (404).");
		System.out.println("asdklfjsdaklfjsaklfjsklafjsklafj");
		
		return new ResponseEntity<Object>(responseBody,HttpStatus.NOT_FOUND);
	}
	
}
