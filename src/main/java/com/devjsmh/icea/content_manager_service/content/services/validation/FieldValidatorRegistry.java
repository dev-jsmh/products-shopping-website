package com.devjsmh.icea.content_manager_service.content.services.validation;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Holds a registry of validator for all supported field types
 * 
 * @author Jhonatan Samuel Martinez Hernandez
 */
@Component
public class FieldValidatorRegistry {

    private static final Logger logger = LoggerFactory.getLogger(FieldValidatorRegistry.class);

    private final Map<String, FieldValidator> registry = new HashMap<>();

    /**
     * Adds a validator for a field type
     * 
     * @param type      name of the field type the validator belongs to
     * @param validator class with the validation logic
     */
    public void add(String type, FieldValidator validator) {
        registry.put(type, validator);
        logger.info("A validator was successfully registered for the field type: {}", type);
    }

    /**
     * Gets the validator class for a supported field type
     * 
     * @param type
     * @return
     */
    public FieldValidator get(String type) {

        FieldValidator validator = this.registry.get(type);

        if (validator == null) {
            throw new RuntimeException("There is no validator registered for field type: " + type);
        }

        return validator;
    }

}
