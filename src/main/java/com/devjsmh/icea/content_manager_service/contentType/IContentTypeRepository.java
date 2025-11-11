package com.devjsmh.icea.content_manager_service.contentType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Jhonatan Samuel Martinez
 */

@Repository
public interface IContentTypeRepository extends JpaRepository<ContentTypeEntity, Long> {

}