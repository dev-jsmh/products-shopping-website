package com.devjsmh.icea.content_manager_service.content;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import com.devjsmh.icea.content_manager_service.content.dtos.ContentDetailedDto;
import com.devjsmh.icea.content_manager_service.content.mappers.IContentWithContentTypeSummaryMapper;
import com.devjsmh.icea.content_manager_service.content.services.ContentTypeField;
import com.devjsmh.icea.content_manager_service.content.services.ContentValidationService;
import com.devjsmh.icea.content_manager_service.contentType.ContentTypeEntity;
import com.devjsmh.icea.content_manager_service.contentType.IContentTypeRepository;
import com.devjsmh.icea.content_manager_service.core.Exceptions.ContentFieldSchemaNotValidException;
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
    private final IContentWithContentTypeSummaryMapper contentWithContentTypeMapper;
    private final ContentValidationService contentValidator;

    @PersistenceContext
    private EntityManager em;

    /**
     * Main constructor where all dependencies should be injected
     * 
     * @param contentRepository
     */
    public ContentService(
            IContentRepository contentRepository,
            IContentTypeRepository contentTypeRepository,
            IContentWithContentTypeSummaryMapper contentWithContentTypeMapper,
            ContentValidationService contentValidator) {
        this.contentRepository = contentRepository;
        this.contentTypeRepository = contentTypeRepository;
        this.contentWithContentTypeMapper = contentWithContentTypeMapper;
        this.contentValidator = contentValidator;
    }

    /**
     * Gets all content entry existing from the database
     * 
     * @return a list of entries
     * @version 1
     */
    public List<ContentEntity> getAllV1(String contentTypeSlug) {

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

        List<ContentTypeField> fields = type.getFields();
        List<Map<String, Object>> data = request.getData();

        List<String> errors = this.contentValidator.validate(fields, data);

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
        request.setCreatedAt(LocalDateTime.now());

        String status = request.getStatus();

        if (status == null || status.isEmpty()) {
            status = ContentStatus.DRAFT.getValue();
            request.setStatus(status);
        }

        if (!ContentStatus.exists(status)) {
            throw new RuntimeException("Content status: '" + status + "' not supported");
        }

        if (status.equals(ContentStatus.PUBLISHED.getValue())) {
            request.setPublishedAt(LocalDateTime.now());
        }

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

        // TODO - filter the content search by status

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

        if (content.getContentType() == null) {
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

        List<ContentTypeField> fields = type.getFields();
        List<Map<String, Object>> data = request.getData();

        List<String> errors = this.contentValidator.validate(fields, data);

        if (!errors.isEmpty()) {

            throw new ContentFieldSchemaNotValidException(id, type.getName(), errors);
        }

        String status = request.getStatus();

        if (status != null) {
            if (!ContentStatus.exists(status)) {
                throw new RuntimeException("Content status: '" + status + "' not supported");
            }

            content.setStatus(status);
        }

        content.setData(request.getData());
        content.setUpdatedAt(LocalDateTime.now());

        if (status.equals(ContentStatus.PUBLISHED.getValue())) {
            content.setPublishedAt(LocalDateTime.now());
        }

        return this.contentRepository.save(content);
    }

    /**
     * Deletes specified content by type and content id
     * 
     * @param slug of the content type
     * @param id   of the content entry
     */
    public void deleteByTypeAndIdV1(String slug, Long id) {

        // check if the content entry exists by getting it from database
        this.getByTypeAndIdV1(slug, id);
        // delete the content
        this.contentRepository.deleteById(id);
    }

    public List<ContentEntity> getAllPublishedContentByType(String contentTypeSlug) {

        ContentTypeEntity type = this.findContentTypeBySlug(contentTypeSlug)
                .orElseThrow(() -> new NoSuchEntityExistsException(
                        "contentType",
                        "slug",
                        contentTypeSlug));

        String status = ContentStatus.PUBLISHED.getValue();
        List<ContentEntity> result = this.findAllByTypeAndStatus(contentTypeSlug, status);
        return result;
    }

    public ContentEntity getPublishedContentByTypeAndId(String contentTypeSlug, Long id) {

        ContentTypeEntity type = this.findContentTypeBySlug(contentTypeSlug)
                .orElseThrow(() -> new NoSuchEntityExistsException(
                        "contentType",
                        "slug",
                        contentTypeSlug));

        ContentEntity content = this.contentRepository.findById(id)
                .orElseThrow(() -> new NoSuchEntityExistsException(
                        "content",
                        "id",
                        id.toString()));

        if (content.getContentType() == null) {
            throw new NullPointerException("The content has no type assigned. the value is null");
        }

        // check if the entity type is the same as the type it's being request
        if (!content.getContentType().getSlug().equals(contentTypeSlug)) {
            String err = "Content type mismatch: the requested type of content was: " + contentTypeSlug
                    + ", but the found content is of type: "
                    + content.getContentType().getSlug();

            throw new RuntimeException(err);
        }

        if (!ContentStatus.PUBLISHED.getValue().equals(content.getStatus())) {
            throw new RuntimeException("There is no published '" + contentTypeSlug + "' content with id: " + id);
        }

        // return result
        return content;
    }

    /**
     * Finds all the content by type and status and return a paginated
     * results
     * 
     * @param contentTypeSlug
     * @param page
     * @param size
     * @param status ex: "draft" or "published"
     * @return paginated result of found content entries
     */
    public Page<ContentDetailedDto> filterPaginatedContent(String contentTypeSlug, Integer page, Integer size,
            String status) {

        if (contentTypeSlug == null) {
            throw new IllegalArgumentException("The 'slug' from the content type must not be null");
        }

        Optional<ContentTypeEntity> type = this.findContentTypeBySlug(contentTypeSlug);

        if (type.isEmpty()) {
            throw new NoSuchEntityExistsException("ContentType", "slug", contentTypeSlug);
        }

        if (page == null) {
            page = 0;
        }

        if (size == null) {
            size = 10;
        }

        PageRequest pageRequest = PageRequest.of(page, size);

        if (status != null) {

            Page<ContentEntity> pagedResult = this.findAllByTypeAndStatus(contentTypeSlug, pageRequest, status);
            List<ContentDetailedDto> dtoList = this.contentWithContentTypeMapper.toDtoList(pagedResult.getContent());
            return new PageImpl<>(dtoList, pageRequest, pagedResult.getTotalElements());
        }

        // by default the content will only be paginated
        Page<ContentEntity> pagedResult = this.findAllByType(contentTypeSlug, pageRequest);
        List<ContentDetailedDto> dtoList = this.contentWithContentTypeMapper.toDtoList(pagedResult.getContent());
        return new PageImpl<>(dtoList, pageRequest, pagedResult.getTotalElements());
    }

    // ===== REPOSITORY METHODS
    //
    // these are repository methods they must be place in the repository layer

    /**
     * Return the total count of contents using their type and status properties
     * 
     * @param contentTypeSlug
     * @param status
     * @return count of found elements
     */
    public long countByTypeAndStatus(String contentTypeSlug, String status) {

        // ---- fetch total count ----
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ContentEntity> countRoot = countQuery.from(ContentEntity.class);

        Join<ContentEntity, ContentTypeEntity> countJoin = countRoot.join("contentType", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<Predicate>();
        // add predicates
        predicates.add(cb.equal(countJoin.get("slug"), contentTypeSlug));
        predicates.add(cb.equal(countRoot.get("status"), status));
        // apply conditionas
        countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[0]));
        // execute the query
        Long totalCount = em.createQuery(countQuery).getSingleResult();

        return totalCount;
    }

    /**
     * Returns the total count of contents based on their type
     * 
     * @param contentTypeSlug
     * @return count of found elements
     */
    public long countByType(String contentTypeSlug) {

        // ---- fetch total count ----
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<ContentEntity> countRoot = countQuery.from(ContentEntity.class);

        Join<ContentEntity, ContentTypeEntity> countJoin = countRoot.join("contentType", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<Predicate>();
        // add predicates
        predicates.add(cb.equal(countJoin.get("slug"), contentTypeSlug));
        // apply conditionas
        countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[0]));
        // execute the query
        Long totalCount = em.createQuery(countQuery).getSingleResult();

        return totalCount;
    }

    public Optional<ContentEntity> findByIdAndType(Long id, String contentTypeSlug) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ContentEntity> cq = cb.createQuery(ContentEntity.class);
        Root<ContentEntity> root = cq.from(ContentEntity.class);
        // join with the contentType entities
        Join<ContentEntity, ContentTypeEntity> contentTypeJoin = root.join("contentType", JoinType.INNER);
        // add all the predicates and queries needed
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get("id"), id));
        predicates.add(cb.equal(contentTypeJoin.get("slug"), contentTypeSlug));

        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<ContentEntity> query = em.createQuery(cq);
        List<ContentEntity> contentResults = query.setMaxResults(1).getResultList();

        if (contentResults.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(contentResults.get(0));
    }

    public List<ContentEntity> findAllByTypeAndStatus(String contentTypeSlug, String status) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ContentEntity> cq = cb.createQuery(ContentEntity.class);
        Root<ContentEntity> root = cq.from(ContentEntity.class);
        // join with the contentType entities
        Join<ContentEntity, ContentTypeEntity> contentTypeJoin = root.join("contentType", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get("status"), status));
        predicates.add(cb.equal(contentTypeJoin.get("slug"), contentTypeSlug));

        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<ContentEntity> query = em.createQuery(cq);

        List<ContentEntity> result = query.getResultList();

        return result;
    }

    public Page<ContentEntity> findAllByTypeAndStatus(String contentTypeSlug, Pageable pageable, String status) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ContentEntity> cq = cb.createQuery(ContentEntity.class);
        Root<ContentEntity> root = cq.from(ContentEntity.class);
        // join with the contentType entities
        Join<ContentEntity, ContentTypeEntity> contentTypeJoin = root.join("contentType", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(contentTypeJoin.get("slug"), contentTypeSlug));
        predicates.add(cb.equal(root.get("status"), status));

        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<ContentEntity> query = em.createQuery(cq);

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<ContentEntity> result = query.getResultList();

        Long totalCount = this.countByTypeAndStatus(contentTypeSlug, status);

        return new PageImpl<>(result, pageable, totalCount);
    }

    public List<ContentEntity> findAllByType(String contentTypeSlug) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ContentEntity> cq = cb.createQuery(ContentEntity.class);
        Root<ContentEntity> root = cq.from(ContentEntity.class);
        Join<ContentEntity, ContentTypeEntity> contentTypejoin = root.join("contentType", JoinType.INNER);
        // add all the predicates and queries needed
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(contentTypejoin.get("slug"), contentTypeSlug));

        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<ContentEntity> query = em.createQuery(cq);
        List<ContentEntity> result = query.getResultList();

        return result;
    }

    public Page<ContentEntity> findAllByType(String contentTypeSlug, Pageable pageable) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ContentEntity> cq = cb.createQuery(ContentEntity.class);
        Root<ContentEntity> root = cq.from(ContentEntity.class);
        // join with the contentType entities
        Join<ContentEntity, ContentTypeEntity> contentTypeJoin = root.join("contentType", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(contentTypeJoin.get("slug"), contentTypeSlug));

        cq.where(predicates.toArray(new Predicate[0]));

        TypedQuery<ContentEntity> query = em.createQuery(cq);

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<ContentEntity> result = query.getResultList();

        Long totalCount = this.countByType(contentTypeSlug);

        return new PageImpl<>(result, pageable, totalCount);
    }

    public Optional<ContentTypeEntity> findContentTypeBySlug(String contentTypeSlug) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ContentTypeEntity> typecq = cb.createQuery(ContentTypeEntity.class);
        Root<ContentTypeEntity> contentTypeRoot = typecq.from(ContentTypeEntity.class);

        typecq.where(cb.equal(contentTypeRoot.get("slug"), contentTypeSlug));

        TypedQuery<ContentTypeEntity> typeQuery = em.createQuery(typecq);
        List<ContentTypeEntity> typeResults = typeQuery
                .setMaxResults(1)
                .getResultList();

        if (typeResults.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(typeResults.get(0));
    }

}