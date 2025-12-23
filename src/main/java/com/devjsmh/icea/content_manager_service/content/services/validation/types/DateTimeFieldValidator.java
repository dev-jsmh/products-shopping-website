package com.devjsmh.icea.content_manager_service.content.services.validation.types;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.devjsmh.icea.content_manager_service.content.services.validation.FieldValidator;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class DateTimeFieldValidator implements FieldValidator {

    @Override
    public String validate(String fieldName, JsonNode value) {

        if (!value.isTextual()) {
            return "Field '" + fieldName + "' must be a datetime";
        }

        String text = value.asText();

        try {
            LocalDateTime.parse(text);
        } catch (Exception e) {
            return "Field '" + fieldName + "' must be a valid ISO datetime";
        }

        return null;

    }

}
