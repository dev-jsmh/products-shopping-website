package com.devjsmh.icea.content_manager_service.content;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devjsmh.icea.content_manager_service.contentType.ContentTypeEntity;
import com.devjsmh.icea.content_manager_service.contentType.IContentTypeRepository;
import com.devjsmh.icea.content_manager_service.core.Exceptions.NoSuchEntityExistsException;

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

        // check if the entity type is the same as the type it's being request
        if (!content.getContentType().getSlug().equals(slug)) {
            String err = "Content type mismatch: the requested type of content was: " + slug
                    + ", but the found content is of type: "
                    + content.getContentType().getSlug();

            throw new RuntimeException(err);
        }

        return content;
    }

    // TODO - create method to update the content entry
    // TODO - create method to delete content entry

}
