package com.devjsmh.icea.content_manager_service.content.dtos;

import com.devjsmh.icea.content_manager_service.contentType.dtos.ContentTypeDto;
import com.fasterxml.jackson.databind.JsonNode;

public class ContentDto {

    private Long id;
    private ContentTypeDto type;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String publishedAt;
    private JsonNode data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContentTypeDto getType() {
        return type;
    }

    public void setType(ContentTypeDto type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public JsonNode getData() {
        return data;
    }

    public void setData(JsonNode data) {
        this.data = data;
    }

}
