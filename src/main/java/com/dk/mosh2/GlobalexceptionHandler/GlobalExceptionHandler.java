package com.dk.mosh2.GlobalexceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.dk.mosh2.exceptions.AccessDeniedException;
import com.dk.mosh2.exceptions.EmptyResourceException;
import com.dk.mosh2.service.exception.PaymentGateWayException;
import com.dk.mosh2.exceptions.ResourceNotFoundException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.dk.mosh2.dto.errorDto.ErrorDto;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> validationExceptionHandler(MethodArgumentNotValidException ex){
		var errorMap  = new HashMap<>(); 
		ex.getBindingResult().getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.badRequest().body(errorMap);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex){
		
		return ResponseEntity.badRequest().body(Map.of("error",ex.getMessage()));
	}
	
	@ExceptionHandler(EmptyResourceException.class)
	public ResponseEntity<?> emptyResource(EmptyResourceException ex){
		
		return ResponseEntity.badRequest().body(Map.of("error",ex.getMessage()));
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> badCredential(BadCredentialsException ex){

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad Credential");
	}
	@ExceptionHandler(JwtException.class)
	public ResponseEntity<?> handleJwtException(JwtException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(Map.of("error", ex.getMessage()));
	}
@ExceptionHandler(MissingRequestHeaderException.class)
public ResponseEntity<?> handleMissingRequestHeader(MissingRequestHeaderException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("error", "Missing required header: " + ex.getHeaderName()));
}

@ExceptionHandler(HttpMessageNotReadableException.class)
public ResponseEntity<?> handleUnreadableMessage(HttpMessageNotReadableException ex){
	return ResponseEntity.badRequest().body(Map.of("error","Invalid Request Body"));
}

@ExceptionHandler(MethodArgumentTypeMismatchException.class)
public ResponseEntity<ErrorDto> handleMethodArgumentTypeMismatch(
        MethodArgumentTypeMismatchException ex,
        HttpServletRequest request) {

    ErrorDto errorDto = ErrorDto.builder()
            .timestamp(LocalDateTime.now().toString())
            .status(HttpStatus.BAD_REQUEST.value())
            .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .message("Invalid value for parameter '" + ex.getName() +
                     "'. Expected type: " + ex.getRequiredType().getSimpleName())
            .path(request.getRequestURI())
            .build();

    return ResponseEntity.badRequest().body(errorDto);
}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> accessDeniedExceptionHandler(AccessDeniedException ex){

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error",ex.getMessage()));
	}

	@ExceptionHandler(PaymentGateWayException.class)
	public ResponseEntity<?> handlerPaymentGateWay(PaymentGateWayException ex){

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error",ex.getMessage()));
	}

}
