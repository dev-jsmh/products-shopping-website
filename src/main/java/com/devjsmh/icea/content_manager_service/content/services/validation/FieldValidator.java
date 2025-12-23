package com.devjsmh.icea.content_manager_service.content.services.validation;


import com.fasterxml.jackson.databind.JsonNode;

public interface FieldValidator {

    String validate(String fieldName, JsonNode value);

}
