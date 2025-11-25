package com.socex.social_extractor.adapters.inbound.web.exception;

import com.socex.social_extractor.adapters.inbound.web.dto.error.ErrorResponse;
import com.socex.social_extractor.adapters.inbound.web.dto.error.ValidationError;
import com.socex.social_extractor.adapters.inbound.web.dto.platform.PlatformResponse;
import com.socex.social_extractor.domain.exception.DomainValidationException;
import com.socex.social_extractor.domain.exception.ResourceAlreadyExistsException;
import com.socex.social_extractor.domain.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<PlatformResponse<ErrorResponse>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return buildResponse(
                "RESOURCE_NOT_FOUND",
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<PlatformResponse<ErrorResponse>> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        return buildResponse(
                "RESOURCE_ALREADY_EXISTS",
                ex.getMessage(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<PlatformResponse<ErrorResponse>> handleDomainValidationException(DomainValidationException ex) {
        return buildResponse(
                "DOMAIN_VALIDATION_ERROR",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<PlatformResponse<ErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ValidationError> validationErrors = ex
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> new ValidationError(
                        ((FieldError) error).getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        ErrorResponse errorData = new ErrorResponse("INPUT_VALIDATION_ERROR", validationErrors);

        PlatformResponse<ErrorResponse> response = new PlatformResponse<>(
                "error",
                "Input validation failed",
                errorData
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<PlatformResponse<ErrorResponse>> handleGeneralException(Exception ex) {
        return buildResponse(
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<PlatformResponse<ErrorResponse>> buildResponse(String errorCode, String message, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(errorCode);
        PlatformResponse<ErrorResponse> response = new PlatformResponse<>(
                "error",
                message,
                errorResponse
        );
        return new ResponseEntity<>(response, status);
    }
}
