package com.devjsmh.icea.content_manager_service.contentType;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "content_types")
public class ContentTypeEntity {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(255)", unique = true)
    private String slug;

    @Column(columnDefinition = "varchar(255)")
    private String name;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String description;

    // this property is a json object
    @Convert(converter = JsonAttributeMapper.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private JsonNode fields;

    // class constructor
    public ContentTypeEntity() {
    }

    // ===================== getters and setters =====================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
