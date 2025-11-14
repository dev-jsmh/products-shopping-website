package com.devjsmh.icea.content_manager_service.core.Exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Represents an error when a given entity is not found
 * 
 * @author Jhonatan Samuel Martinez <developer.jhonatan.martinez@gmail.com>
 */

public class NoSuchEntityExistsException extends RuntimeException {

    private static final Logger logger = LoggerFactory.getLogger(NoSuchEntityExistsException.class);
    private final String entityName;
    private final String searchField;
    private final String fieldValue;

    /**
     * Main constructor to create exceptions when an entity is not found by a
     * specific field.
     * generates detail message and logs warning.
     * 
     * @param entityName  Name of the entity that was not found (eg, "User",
     *                    "contentType")
     * @param searchField Name of the field used to search the entity (eg, "id",
     *                    "name")
     * @param fieldValue  value used in the search field (eg, "blog", "Jhon")
     */
    public NoSuchEntityExistsException(String entityName, String searchField, String fieldValue) {
        super(String.format("Entity '%s' was not found with '%s: '%s'", entityName, searchField, fieldValue));
        this.entityName = entityName;
        this.searchField = searchField;
        this.fieldValue = fieldValue;

        // Logs the message as a warning in the application logs
        logger.warn("Entity not found: {} with {}: {}", entityName, searchField, fieldValue);
    }

    /**
     * Secondary constructor to create exceptions with a customized message.
     * It is prefered for when the search details are irrelevant or a more
     * general message is prefered.
     * 
     * @param message Descriptive text about the error
     */
    public NoSuchEntityExistsException(String message) {
        super(message);
        this.entityName = null; // it is set to null as search field and value are not provided
        this.searchField = null;
        this.fieldValue = null;

        // Logs the message as a warning in the application logs
        logger.warn("Entity not found: {}", message);
    }

    public String getEntityName() {
        return entityName;
    }

    public String getSearchField() {
        return searchField;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}
