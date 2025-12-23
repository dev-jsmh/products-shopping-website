package com.devjsmh.icea.content_manager_service.content.dtos;

import java.util.List;
import java.util.Map;

import com.devjsmh.icea.content_manager_service.contentType.dtos.ContentTypeSummaryDto;

public class ContentDetailedDto {

    private Long id;
    private ContentTypeSummaryDto type;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String publishedAt;
    private List<Map<String, Object>> data;

    // ================ getters and setters ================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContentTypeSummaryDto getType() {
        return type;
    }

    public void setType(ContentTypeSummaryDto type) {
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

    public void setUpdatedAt(String updateAt) {
        this.updatedAt = updateAt;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

}
