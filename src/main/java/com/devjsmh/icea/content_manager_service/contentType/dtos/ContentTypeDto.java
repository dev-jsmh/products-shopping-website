package com.devjsmh.icea.content_manager_service.contentType.dtos;

import java.util.List;

import com.devjsmh.icea.content_manager_service.content.services.ContentTypeField;


public class ContentTypeDto {

    private Long id;
    private String name;
    private String slug;
    private String description;
    private List<ContentTypeField> fields;

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

    public List<ContentTypeField> getFields() {
        return fields;
    }

    public void setFields(List<ContentTypeField> fields) {
        this.fields = fields;
    }

}
