package com.devjsmh.icea.content_manager_service.content.services;

import java.util.Map;

/**
 * This is a base class for creating other block types to be used
 * in a content
 */
public class ContentTypeField {

    public String name;
    public String type;
    public boolean isRequired = false;
    public Map<String, Object> schema = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }

    public Map<String, Object> getSchema() {
        return schema;
    }

    public void setSchema(Map<String, Object> schema) {
        this.schema = schema;
    }

}
