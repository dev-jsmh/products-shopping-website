package com.devjsmh.icea.content_manager_service.content.services.validation.types;

import org.springframework.stereotype.Component;

import com.devjsmh.icea.content_manager_service.content.services.validation.FieldValidator;
import com.fasterxml.jackson.databind.JsonNode;

@Component
public class MediaField implements FieldValidator{

    @Override
    public String validate(String fieldName, JsonNode value){

        if (value.isEmpty()) {
            return "Field '" + fieldName + "' must not be null";
        }

        if (!value.has("id")) {
            return "Field '" + fieldName + "' must contain a propery 'id'";
        }

        if (value.get("id") == null && !value.isTextual()) {
            return "Field '" + fieldName + "' must contain a valid id string";
        }

        if (!value.has("url")) {
            return "Field '" + fieldName + "' must contain a property 'url'";
        }

        if (value.get("url") == null && !value.isTextual()) {
            return "Field '" + fieldName + "' must contain a valid 'url' string";
        }

        return null;
    }

}
