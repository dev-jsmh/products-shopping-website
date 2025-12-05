package com.devjsmh.icea.content_manager_service.core.models;

import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Represents a standarized error response for the clients.
 * 
 * @author Jhonatan Samuel Martinez developer.jhonatan.martinez@gmail.com
 * 
 */

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ApiErrorResponse {

    // tells jackson how to format the timestamp field
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp; // Date and time when the error occurred.
    private int status; // HTTP status code of the error (eg, 404, 403, 304).
    private String error; // Description of the HTTP status eg, "Not Found", "Forbiden".
    private String message; // User friendly error message
    private String path; // The url of the request that generated the error
    private Map<String, Object> details; // Additional error details (optional)

    /**
     * Constructor for basic errors
     * 
     * @param status  The HTTP status (eg, 404, 500, 304)
     * @param error   Phrase that descrives the HTTP status (eg, "Not Found",
     *                "Forbiden")
     * @param message Descriptive error message
     * @param path    The URL that was requested and cause the error
     */
    public ApiErrorResponse(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor for errors with specific additional details
     * 
     * @param status  The HTTP status (eg, 404, 500, 304)
     * @param error   Phrase that descrives the HTTP status (eg, "Not Found",
     *                "Forbiden")
     * @param message Descriptive error message
     * @param path    The URL that was requested and cause the error
     * @param details A Map with specific details about the exception
     */
    public ApiErrorResponse(int status, String error, String message, String path, Map<String, Object> details) {
        this(status, error, message, path);
        this.details = details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

}
