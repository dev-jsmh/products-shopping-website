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
     * Finds a content entry that belongs to a specified type by content id
     * 
     * ex: getbySlug("blog-post", 3928323)
     * 
     * @param slug from the content-type
     * @param id   of the content entry
     * @return the found content entry
     */
    public ContentEntity getBySlugV1(String slug, Long id) {

        // Todo - handle exection when the param is not provided
        // what exception can i throw

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ContentEntity> cq = cb.createQuery(ContentEntity.class);
        Root<ContentEntity> contents = cq.from(ContentEntity.class);

        // joins
        Join<ContentEntity, ContentTypeEntity> contentTypejoin = contents.join("contentType", JoinType.INNER);

        // add all the predicates and queries needed
        Predicate slugEqualPr = cb.equal(contentTypejoin.get("slug"), slug);
        Predicate contentIdEqualsPr = cb.equal(contents.get("id"), id);

        cq.where(slugEqualPr, contentIdEqualsPr);

        TypedQuery<ContentEntity> q = em.createQuery(cq);
        ContentEntity obj = q.setFirstResult(0)
                .setMaxResults(1)
                .getSingleResult();

        // close the instances
        em.close();

        return obj;
    }

    // TODO - create method to update the content entry
    // TODO - create method to delete content entry

}
