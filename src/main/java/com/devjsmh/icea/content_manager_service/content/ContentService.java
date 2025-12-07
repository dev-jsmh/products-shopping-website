package com.devjsmh.icea.content_manager_service.content;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.devjsmh.icea.content_manager_service.contentType.ContentTypeEntity;
import com.devjsmh.icea.content_manager_service.contentType.IContentTypeRepository;
import com.devjsmh.icea.content_manager_service.core.Exceptions.ContentFieldSchemaNotValidException;
import com.devjsmh.icea.content_manager_service.core.Exceptions.NoSuchEntityExistsException;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * 
 * This service will contain all bussines logic related to content
 * management
 * 
 * @author Jhonatan Samuel Martinez
 */
@Service
public class ContentService {

    private final IContentRepository contentRepository;
    private final IContentTypeRepository contentTypeRepository;

    @PersistenceContext
    private EntityManager em;

    /**
     * Main constructor where all dependencies should be injected
     * 
     * @param contentRepository
     */
    public ContentService(IContentRepository contentRepository, IContentTypeRepository contentTypeRepository) {
        this.contentRepository = contentRepository;
        this.contentTypeRepository = contentTypeRepository;
    }

    /**
     * Gets all content entry existing from the database
     * 
     * @return a list of entries
     * @version 1
     */
    public List<ContentEntity> getAllV1(String contentTypeSlug) {

        // handle null parameters

        // -------------------- criteria api ----------------

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ContentEntity> cq = cb.createQuery(ContentEntity.class);
        Root<ContentEntity> root = cq.from(ContentEntity.class);

        // joins
        Join<ContentEntity, ContentTypeEntity> contentTypejoin = root.join("contentType", JoinType.INNER);

        // add all the predicates and queries needed
        Predicate slugEqualPr = cb.equal(contentTypejoin.get("slug"), contentTypeSlug);

        cq.where(slugEqualPr);

        TypedQuery<ContentEntity> q = em.createQuery(cq);
        List<ContentEntity> list = q.getResultList();

        // close the instances
        em.close();

        return list;
    }

    /**
     * Save a new content entry in the database
     * 
     * @param slug    the type of content to be created
     * @param request the new entry to be saved
     * @return the saved entry
     * @version 1
     */
    public ContentEntity createV1(String contentTypeSlug, ContentEntity request) {

        // check if the content-type exists
        // if yes then create a new content entry
        // else throw error as the type of content does not exists
        ContentTypeEntity type = this.contentTypeRepository.findBySlug(contentTypeSlug)
                .orElseThrow(() -> new NoSuchEntityExistsException(
                        "ContentType",
                        "slug",
                        contentTypeSlug));

        // validate that the content has all the fields from the content type schema
        // if the content data do not match the content type schema
        // throw error and named the fields that are missing

        if (request.getData().isEmpty()) {
            throw new RuntimeException("The \"data\" property is required");
        }

        if (request.getData().isArray() != true) {
            throw new RuntimeException("The \"data\" property must be an array of objects");
        }

        JsonNode fields = type.getFields();
        JsonNode data = request.getData();

        List<String> errors = this.validate(fields, data);

        if (!errors.isEmpty()) {

            StringBuilder stb = new StringBuilder();
            String st1 = "Cannot create a new content of type:" + type.getName();
            String st2 = "Invalid content data: ";
            String st3 = " the data does not meet field schema from the content type";
            stb.append(st1);
            stb.append(st2);
            stb.append(st3);

            throw new ContentFieldSchemaNotValidException(stb.toString(), errors);
        }

        // set the type
        request.setContentType(type);

        return this.contentRepository.save(request);
    }

    /**
     * 
     * Finds a content entry by type and by content id
     * 
     * ex: getByTypeAndId("blog-post", 3928323)
     * 
     * @param slug from the content-type
     * @param id   of the content entry
     * @return the found content entry
     */
    public ContentEntity getByTypeAndIdV1(String slug, Long id) {

        if (slug == null) {
            throw new IllegalArgumentException("slug must not be null or blank");
        }

        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }

        // --------- Fetch ContentEntity -------
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ContentEntity> cq = cb.createQuery(ContentEntity.class);
        Root<ContentEntity> contents = cq.from(ContentEntity.class);
        // add all the predicates and queries needed
        cq.where(cb.equal(contents.get("id"), id));

        TypedQuery<ContentEntity> q = em.createQuery(cq);
        List<ContentEntity> contentResults = q.setMaxResults(1).getResultList();

        if (contentResults.isEmpty()) {
            throw new NoSuchEntityExistsException("ContentEntity", "id", id.toString());
        }

        ContentEntity content = contentResults.get(0);

        // --------- Fetch ContentTypeEntity -------
        CriteriaQuery<ContentTypeEntity> typecq = cb.createQuery(ContentTypeEntity.class);
        Root<ContentTypeEntity> contentTypeRoot = typecq.from(ContentTypeEntity.class);
        typecq.where(cb.equal(contentTypeRoot.get("slug"), slug));

        TypedQuery<ContentTypeEntity> typeQuery = em.createQuery(typecq);
        List<ContentTypeEntity> typeResults = typeQuery.setMaxResults(1).getResultList();

        if (typeResults.isEmpty()) {
            throw new NoSuchEntityExistsException("ContentTypeEntity", "slug", slug);
        }

        if(content.getContentType() == null){
            throw new NullPointerException("The content has no type assigned. the value is null");
        }

        // check if the entity type is the same as the type it's being request
        if (!content.getContentType().getSlug().equals(slug)) {
            String err = "Content type mismatch: the requested type of content was: " + slug
                    + ", but the found content is of type: "
                    + content.getContentType().getSlug();

            throw new RuntimeException(err);
        }

        return content;
    }

    /**
     * Updates a existing content entry
     * 
     * @param slug    of the content type
     * @param id      of the content entry
     * @param request represent the object with new data
     * @return the updated content
     */
    public ContentEntity updateByTypeAndIdV1(String contentTypeSlug, Long id, ContentEntity request) {

        ContentEntity content = this.getByTypeAndIdV1(contentTypeSlug, id);

        ContentTypeEntity type = content.getContentType();

        JsonNode fields = type.getFields();
        JsonNode data = request.getData();

        List<String> errors = this.validate(fields, data);

        if (!errors.isEmpty()) {

            throw new ContentFieldSchemaNotValidException(id, type.getName(), errors);
        }

        content.setStatus(request.getStatus());
        content.setData(request.getData());
        content.setUpdatedAt(LocalDateTime.now());

        if ("published".equals(request.getStatus())) {
            content.setPublishedAt(LocalDateTime.now());
        }

        return this.contentRepository.save(content);
    }

    /**
     * Deletes specified content by type and content id
     * 
     * @param slug of the content type
     * @param id of the content entry
     */
    public void deleteByTypeAndIdV1(String slug, Long id) {

        // check if the content entry exists by getting it from database
        this.getByTypeAndIdV1(slug, id);
        // delete the content
        this.contentRepository.deleteById(id);
    }

    public List<String> validate(JsonNode fieldSchema, JsonNode data) {

        List<String> errors = new ArrayList<>();

        if (fieldSchema.isEmpty()) {
            String err = "Content type schema is empty: There are no fields schema";
            errors.add(err);
        }

        if (!fieldSchema.isArray()) {
            String err = "Content type schema is invalid: Fields must be an array";
            errors.add(err);
        }

        for (JsonNode fieldDefinition : fieldSchema) {
            String err = this.validateField(fieldDefinition, data);
            if (err != null) {
                errors.add(err);
            }
        }

        return errors;
    }

    public String validateField(JsonNode fieldDefinition, JsonNode data) {

        String fieldName = fieldDefinition.get("name").asText();
        String fieldType = fieldDefinition.get("type").asText();
        boolean isRequired = fieldDefinition.get("required").asBoolean(false);

        // get from content data
        JsonNode value = this.getFieldByName(fieldName, data);

        // -------- REQUIRED CHECK ----------
        if (isRequired && (value == null || value.isNull())) {
            return "Missing required field: " + fieldName;
        }

        if (value == null || value.isNull()) {
            return "Missing field: " + fieldName;
        }

        // ------- TYPE CHECK ------
        switch (fieldType) {
            case "string":
                if (!value.isTextual()) {
                    return "Field '" + fieldName + "' must be a string";
                }

                break;

            case "text":
                if (!value.isTextual()) {
                    return "Field '" + fieldName + "' must be a text";
                }

                break;

            case "richtext":
                if (!value.isTextual()) {
                    return "Field '" + fieldName + "' must be a richtext";
                }

                break;

            case "datetime":
                if (!value.isTextual()) {
                    return "Field '" + fieldName + "' must be a datetime";

                }
                return validateDateTimeField(fieldName, value.asText());

            case "media":
                return validateMediaField(fieldName, value);

            default:
                return "Unsupported fiel type: " + fieldType + " for field: " + fieldName;
        }

        return null;

    }

    public String validateDateTimeField(String fieldName, String text) {
        try {
            LocalDateTime.parse(text);
        } catch (Exception e) {
            return "Field '" + fieldName + "' must be a valid ISO datetime";
        }

        return null;
    }

    public String validateMediaField(String fieldName, JsonNode value) {

        if (!value.isObject()) {
            return "Field '" + fieldName + "' must be an object";
        }

        if (!value.has("id")) {
            return "Field '" + fieldName + "' must contain a propery 'id'";
        }

        if (value.get("id") != null && !value.get("id").isTextual()) {
            return "Field '" + fieldName + "' must contain a valid id string";
        }

        if (!value.has("url")) {
            return "Field '" + fieldName + "' must contain a property 'url'";
        }

        if (value.get("url") != null && !value.get("url").isTextual()) {
            return "Field '" + fieldName + "' must contain a valid 'url' string";
        }

        return null;
    }

    public JsonNode getFieldByName(String name, JsonNode contentArray) {

        JsonNode value = null;

        for (JsonNode contentField : contentArray) {

            if (contentField.has(name)) {
                value = contentField.get(name);
                break;
            }
        }

        return value;
    }
}