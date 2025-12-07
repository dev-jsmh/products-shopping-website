package com.devjsmh.icea.content_manager_service.core.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentFieldSchemaNotValidException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(ContentFieldSchemaNotValidException.class);
    private Long contentId;
    private String contentType;
    private Object errors;

    /**
     * Main constructore to create exceptions when a content data does not meet
     * the content type field schema
     * 
     * @param contentId   id of the content
     * @param contentType type of the content
     */
    public ContentFieldSchemaNotValidException(Long contentId, String contentType) {
        super(String.format("The content id: {} does not meet the field schema from the type: {}", contentId, contentType));
        this.contentId = contentId;
        this.contentType = contentType;
        this.errors = null;

        // logs the warning message to the application log
        logger.warn("The content id: {} does not meet the field schema from the type: {}", contentId, contentType);
    }

    /**
     * Creates exceptions with more details about what has caused it
     * 
     * @param contentId   id of the content
     * @param contentType type of the content
     * @param errors a lists or a hashmap object with the error messages or stack trace
     */
    public ContentFieldSchemaNotValidException(Long contentId, String contentType, Object errors) {
        super(String.format("The content id: {} does not meet the field schema from the type: {}", contentId, contentType));
        this.contentId = contentId;
        this.contentType = contentType;
        this.errors = errors;

        // logs the warning message to the application log
        logger.warn("The content id: {} does not meet the field schema from the type: {}", contentId, contentType);
    }

    /**
     * Creates an exception with a general message
     * it is for situations where a more general message is prefered
     * 
     * @param message general message
     */
    public ContentFieldSchemaNotValidException(String message) {
        super(message);
        this.contentId = null;
        this.contentType = null;
        this.errors = null;
    }

    /**
     * Creates an exception with a custom message and error details
     * 
     * @param message
     * @param errors
     */
    public ContentFieldSchemaNotValidException(String message, Object errors) {
        super(message);
        this.errors = errors;
        this.contentId = null;
        this.contentType = null;

        logger.warn(message);
    }

    public Object getDetails() {
        return errors;
    }

    public void setDetails(Object errors) {
        this.errors = errors;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

}
