package com.devjsmh.icea.content_manager_service.content;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 * This interface will allow to interact with the database and manipulate 
 * the content entity. operations like save, find, delete, etc are provided
 * 
 * @author Jhonatan Samuel Martinez
 */

@Repository
public interface IContentRepository extends JpaRepository<ContentEntity, Long> {


}
