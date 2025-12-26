package com.devjsmh.icea.content_manager_service.contentType.dtos;

import com.fasterxml.jackson.databind.JsonNode;


public class ContentTypeDto {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private JsonNode fields;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JsonNode getFields() {
        return fields;
    }

    public void setFields(JsonNode fields) {
        this.fields = fields;
    }

}
