package com.devjsmh.icea.content_manager_service.contentType;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Jhonatan Samuel Martinez
 */

@Repository
public interface IContentTypeRepository extends JpaRepository<ContentTypeEntity, Long> {

    /**
	 * Retrieves an entity by its slug.
	 *
	 * @param slug must not be {@literal null}.
	 * @return the entity with the given slug or {@literal Optional#empty()} if none found.
	 * @throws IllegalArgumentException if {@literal slug} is {@literal null}.
	 */
    Optional<ContentTypeEntity> findBySlug(String slug);

}