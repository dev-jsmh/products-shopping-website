package com.devjsmh.icea.content_manager_service.contentType;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;

/**
 * @author Jhonatan Samuel Martinez
 */

public class JsonAttributeMapper implements AttributeConverter<JsonNode, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(JsonNode attributes) {

        try {
            return mapper.writeValueAsString(attributes);

        } catch (IOException ex) {
            throw new IllegalArgumentException("Error converting JsonNode to String", ex);
        }
    }

    @Override
    public JsonNode convertToEntityAttribute(String dbData) {

        try {

            return mapper.readValue(dbData, new TypeReference<JsonNode>() {});
        } catch (IOException ex) {
            throw new IllegalArgumentException("Error converting JsonNode to String", ex);

        }
    }

}