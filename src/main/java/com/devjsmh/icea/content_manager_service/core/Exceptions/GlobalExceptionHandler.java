package com.devjsmh.icea.content_manager_service.core.Exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import com.devjsmh.icea.content_manager_service.core.models.ApiErrorResponse;

/**
 * This class will catch global errors from all module of the API
 * and will respond with the correct HTTP response
 * 
 * @author Jhonatan Samuel Martinez
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchEntityExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundEntity(NoSuchEntityExistsException ex,
            ServletWebRequest request) {

        // get the path where the error was cause
        String requestURL = request.getRequest().getRequestURI();

        // add details about the not found entity
        Map<String, Object> details = new HashMap<>();

        if (ex.getEntityName() != null)
            details.put("entity", ex.getEntityName());
        if (ex.getSearchField() != null)
            details.put("field", ex.getSearchField());
        if (ex.getFieldValue() != null)
            details.put("value", ex.getFieldValue().toString());

        // instantiate a new error object
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                requestURL,
                details);

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * 
     * Gets trigger when an endpoint does not get the body of the request if
     * nessesary or it is malformed
     * and returns the corresponding http response.
     * 
     * @param ex      The exception
     * @param request The request
     * @return A HTTP BAD_REQUEST response
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttMessagepNotReadableException(HttpMessageNotReadableException ex,
            ServletWebRequest request) {

        String requestUrl = request.getRequest().getRequestURI();

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                requestUrl);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * When a method gets invalid input
     * 
     * @param ex      The exception
     * @param request The request
     * @return A HTTP BAD_REQUEST response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex,
            ServletWebRequest request) {

        String requestUrl = request.getRequest().getRequestURI();

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Invalid input: " + ex.getMessage(),
                requestUrl);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class) // Catch-all for other exceptions
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception ex, ServletWebRequest request) {

        String requestUrl = request.getRequest().getRequestURI();

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "An unexpected error occurred: " + ex.getMessage(),
                requestUrl);

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ContentFieldSchemaNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(ContentFieldSchemaNotValidException ex,
            ServletWebRequest request) {

        String requestUrl = request.getRequest().getRequestURI();

        Map<String, Object> details = new HashMap<>();

        details.put("errors", ex.getDetails());
        details.put("stackTrace", ex.getStackTrace());

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                requestUrl,
                details);

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // This exception handler belongs to the authentication module
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentialException(BadCredentialsException ex,
            ServletWebRequest request) {

        String requestUrl = request.getRequest().getRequestURI();

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Invalid username or password",
                requestUrl);

        ex.printStackTrace();

        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
}
