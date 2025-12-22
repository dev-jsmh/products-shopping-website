package com.devjsmh.icea.content_manager_service.content.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.devjsmh.icea.content_manager_service.content.services.validation.FieldValidator;
import com.devjsmh.icea.content_manager_service.content.services.validation.FieldValidatorRegistry;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author Jhonatan Samuel Martinez Hernandez
 */
@Service
public class ContentValidationService {

    private final FieldValidatorRegistry validatorsRegistry;

    public ContentValidationService(FieldValidatorRegistry validatorsRegistry) {
        this.validatorsRegistry = validatorsRegistry;
    }

    public List<String> validate(List<ContentTypeField> fieldSchema, List<Map<String, Object>> contentBlocks) {

        List<String> errors = new ArrayList<>();

        if (fieldSchema.isEmpty()) {
            String err = "Content type schema is empty: There are no fields schema";
            errors.add(err);
        }

        for (ContentTypeField fieldDefinition : fieldSchema) {
            String err = this.validateField(fieldDefinition, contentBlocks);
            if (err != null) {
                errors.add(err);
            }
        }

        return errors;
    }

    public String validateField(ContentTypeField fieldDefinition, List<Map<String, Object>> contentBlocks) {

        String fieldName = fieldDefinition.getName();
        String fieldType = fieldDefinition.getType();
        boolean isRequired = fieldDefinition.isRequired();

        // get from content data
        Object block = this.getFieldByName(fieldName, contentBlocks);

        JsonNode value = this.toJson(block);

        // -------- REQUIRED CHECK ----------
        if (isRequired && (value == null || value.isNull())) {
            return "Missing required field: " + fieldName;
        }

        if (value == null || value.isNull()) {
            return "Missing field: " + fieldName;
        }

        FieldValidator validator = validatorsRegistry.get(fieldType);
        return validator.validate(fieldName, value);

    }

    public Object getFieldByName(String name, List<Map<String, Object>> contentArray) {

        Object obj = null;

        for (Map<String, Object> contentField : contentArray) {

            if (contentField.containsKey(name)) {
                obj = contentField.get(name);
                break;
            }
        }

        return obj;
    }

    public JsonNode toJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(obj, JsonNode.class);
    }

    public Object toObj(JsonNode json) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(json, Object.class);
    }

}
