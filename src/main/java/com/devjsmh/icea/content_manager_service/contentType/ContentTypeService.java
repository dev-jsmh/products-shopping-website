package com.devjsmh.icea.content_manager_service.contentType;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

/**
 * @author Jhonatan Samuel Martinez
 */
@Service
public class ContentTypeService {

    private final IContentTypeRepository contentTypeRepository;

    public ContentTypeService(IContentTypeRepository contentTypeRepository) {
        this.contentTypeRepository = contentTypeRepository;
    }

    /**
     * Return all the records save in the database
     * 
     * @version 1
     */
    public List<ContentTypeEntity> getAllV1() {
        return this.contentTypeRepository.findAll();
    }

    /**
     * Saved a new record in the database
     * 
     * @param entity the new content-type to be saved
     * @return the saved record
     * @version 1
     */

    public ContentTypeEntity saveEntityV1(ContentTypeEntity entity) {

        try {
            return contentTypeRepository.save(entity);
        } catch (IllegalArgumentException iaex) {
            throw iaex;
        }
    }

    /**
     * Gets a specific record from database
     * 
     * @param id
     * @return the found record
     */
    public ContentTypeEntity getByIdV1(Long id) {

        try {

            // finds and optional record from the database
            Optional<ContentTypeEntity> OptContentType = this.contentTypeRepository.findById(id);

            if (OptContentType.isPresent()) {
                // the entity is not found
                return OptContentType.get();
            }

            throw new RuntimeException("the record with id " + id + " was not found");

        } catch (Exception ex) {
            throw new RuntimeException("Error when trying to get the content-type with id: " + id + " from database");
        }
    }

    /**
     * Updates a content-type record from database
     * 
     * @param request the object with the updated values
     * @param id      the identifycation of the content-type
     */
    public ContentTypeEntity updateByIdV1(ContentTypeEntity request, Long id) {

        ContentTypeEntity entity = null;
        try {
            // finds and optional record from the database
            Optional<ContentTypeEntity> OptContentType = this.contentTypeRepository.findById(id);

            if (OptContentType.isPresent()) {
                entity = OptContentType.get();
            } else {
                // the entity is not found
                throw new RuntimeException("The record with id " + id + " was not found");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Error when trying to get the content-type with id: " + id + " from database");
        }

        // check if the values are not empty and are different to the original one set
        // before
        if (!request.getSlug().isEmpty() && !request.getSlug().equals(entity.getSlug())) {
            entity.setSlug(request.getSlug());
        }

        if (!request.getName().isEmpty() && !request.getName().equals(entity.getName())) {
            entity.setName(request.getName());
        }

        if (!request.getFields().isEmpty() && !request.getFields().equals(entity.getFields())) {
            entity.setFields(request.getFields());
        }

        if (!request.getDescription().isEmpty() && !request.getDescription().equals(entity.getDescription())) {
            entity.setDescription(request.getDescription());
        }

        try {
            // save the old entity with the new values
            // return the updated entity
            return this.contentTypeRepository.save(entity);
        } catch (IllegalArgumentException iaex) {
            throw iaex;
        }

    }

    /**
     * Deletes a content-type record from database
     * 
     * @param id identityfaction of the record to be deleted
     * @version 1
     */
    public void deleteByIdV1(Long id) {

        try {
            // finds and optional record from the database
            Optional<ContentTypeEntity> OptContentType = this.contentTypeRepository.findById(id);

            if (OptContentType.isEmpty()) {
                // the entity is not found
                throw new RuntimeException("The record with id " + id + " not exists");
            }

        } catch (Exception ex) {
            throw new RuntimeException("Error when trying to get the content-type with id: " + id + " from database");
        }

        try {
            this.contentTypeRepository.deleteById(id);
        } catch (IllegalArgumentException iaex) {
            throw iaex;
        }
    }

}
