package com.devjsmh.icea.content_manager_service.content.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 
 * @author Jhonatan Samuel Martinez Hernandez
 */
@Service
public class ContentValidationService {

    public List<String> validate(JsonNode fieldSchema, JsonNode data) {

        List<String> errors = new ArrayList<>();

        if (fieldSchema.isEmpty()) {
            String err = "Content type schema is empty: There are no fields schema";
            errors.add(err);
        }

        if (!fieldSchema.isArray()) {
            String err = "Content type schema is invalid: Fields must be an array";
            errors.add(err);
        }

        for (JsonNode fieldDefinition : fieldSchema) {
            String err = this.validateField(fieldDefinition, data);
            if (err != null) {
                errors.add(err);
            }
        }

        return errors;
    }

    public String validateField(JsonNode fieldDefinition, JsonNode data) {

        String fieldName = fieldDefinition.get("name").asText();
        String fieldType = fieldDefinition.get("type").asText();
        boolean isRequired = fieldDefinition.get("required").asBoolean(false);

        // get from content data
        JsonNode value = this.getFieldByName(fieldName, data);

        // -------- REQUIRED CHECK ----------
        if (isRequired && (value == null || value.isNull())) {
            return "Missing required field: " + fieldName;
        }

        if (value == null || value.isNull()) {
            return "Missing field: " + fieldName;
        }

        // ------- TYPE CHECK ------
        switch (fieldType) {
            case "string":
                if (!value.isTextual()) {
                    return "Field '" + fieldName + "' must be a string";
                }

                break;

            case "text":
                if (!value.isTextual()) {
                    return "Field '" + fieldName + "' must be a text";
                }

                break;

            case "richtext":
                if (!value.isTextual()) {
                    return "Field '" + fieldName + "' must be a richtext";
                }

                break;

            case "datetime":
                if (!value.isTextual()) {
                    return "Field '" + fieldName + "' must be a datetime";

                }
                return validateDateTimeField(fieldName, value.asText());

            case "media":
                return validateMediaField(fieldName, value);

            default:
                return "Unsupported fiel type: " + fieldType + " for field: " + fieldName;
        }

        return null;

    }

    public String validateDateTimeField(String fieldName, String text) {
        try {
            LocalDateTime.parse(text);
        } catch (Exception e) {
            return "Field '" + fieldName + "' must be a valid ISO datetime";
        }

        return null;
    }

    public String validateMediaField(String fieldName, JsonNode value) {

        if (!value.isObject()) {
            return "Field '" + fieldName + "' must be an object";
        }

        if (!value.has("id")) {
            return "Field '" + fieldName + "' must contain a propery 'id'";
        }

        if (value.get("id") != null && !value.get("id").isTextual()) {
            return "Field '" + fieldName + "' must contain a valid id string";
        }

        if (!value.has("url")) {
            return "Field '" + fieldName + "' must contain a property 'url'";
        }

        if (value.get("url") != null && !value.get("url").isTextual()) {
            return "Field '" + fieldName + "' must contain a valid 'url' string";
        }

        return null;
    }

    public JsonNode getFieldByName(String name, JsonNode contentArray) {

        JsonNode value = null;

        for (JsonNode contentField : contentArray) {

            if (contentField.has(name)) {
                value = contentField.get(name);
                break;
            }
        }

        return value;
    }

}
