package com.devjsmh.icea.content_manager_service.contentType.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.devjsmh.icea.content_manager_service.contentType.ContentTypeEntity;
import com.devjsmh.icea.content_manager_service.contentType.dtos.ContentTypeDto;

@Mapper(componentModel = "spring")
public interface IContentTypeMapper {

    ContentTypeDto toDto(ContentTypeEntity entity);

    ContentTypeEntity toEntity(ContentTypeDto dto);

    List<ContentTypeDto> toDtoList(List<ContentTypeEntity> entityList);

    List<ContentTypeEntity> toEntityList(List<ContentTypeDto> dtoList);

}
