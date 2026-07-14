package com.users.api.controller;

import com.users.api.dto.response.ErrorResponseDTO;
import jakarta.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ApiExceptionHandler {
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponseDTO> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception) {
		return buildUnprocessableEntityResponse(buildFieldErrors(exception.getName(), exception.getName() + " deve ser um número inteiro"));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponseDTO> handleConstraintViolation(ConstraintViolationException exception) {
		Map<String, List<String>> errors = new LinkedHashMap<>();
		exception.getConstraintViolations().forEach(violation -> addError(errors,
				lastPathSegment(violation.getPropertyPath().toString()), violation.getMessage()));
		return buildUnprocessableEntityResponse(errors);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponseDTO> handleMissingServletRequestParameter(MissingServletRequestParameterException exception) {
		String parameterName = exception.getParameterName();
		return buildUnprocessableEntityResponse(buildFieldErrors(parameterName, parameterName + " é obrigatório"));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
		Map<String, List<String>> errors = new LinkedHashMap<>();
		exception.getBindingResult().getFieldErrors().forEach(fieldError -> addError(errors,
				fieldError.getField(), fieldError.getDefaultMessage()));
		return buildUnprocessableEntityResponse(errors);
	}

	private ResponseEntity<ErrorResponseDTO> buildUnprocessableEntityResponse(Map<String, List<String>> errors) {
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ErrorResponseDTO.builder().message("Erro de validação").errors(errors).build());
	}

	private Map<String, List<String>> buildFieldErrors(String field, String message) {
		Map<String, List<String>> errors = new LinkedHashMap<>();
		addError(errors, field, message);
		return errors;
	}

	private void addError(Map<String, List<String>> errors, String field, String message) {
		errors.computeIfAbsent(field == null || field.isBlank() ? "_general" : field, key -> new java.util.ArrayList<>())
				.add(message);
	}

	private String lastPathSegment(String path) {
		if (path == null || path.isBlank()) {
			return "_general";
		}

		String[] parts = path.split("\\.");
		return parts[parts.length - 1];
	}
}
