package com.devjsmh.icea.content_manager_service.contentType.mappers;

import org.mapstruct.Mapper;

import com.devjsmh.icea.content_manager_service.contentType.ContentTypeEntity;
import com.devjsmh.icea.content_manager_service.contentType.dtos.ContentTypeSummaryDto;

@Mapper(componentModel = "spring")
public interface IContentTypeSummaryMapper {

    /**
     * Mapps the content-type to a dto with a reduce number of properties
     * 
     * @param entity
     * @return a dto with the id, name, and slug of the type
     */
    ContentTypeSummaryDto toDto(ContentTypeEntity entity);

}
