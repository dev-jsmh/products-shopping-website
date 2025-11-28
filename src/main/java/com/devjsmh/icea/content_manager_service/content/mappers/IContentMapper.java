package com.devjsmh.icea.content_manager_service.content.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.devjsmh.icea.content_manager_service.content.ContentEntity;
import com.devjsmh.icea.content_manager_service.content.dtos.ContentDto;
import com.devjsmh.icea.content_manager_service.contentType.mappers.IContentTypeMapper;

@Mapper(componentModel = "spring", uses = IContentTypeMapper.class)
public interface IContentMapper {

    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "publishedAt", target = "publishedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "contentType", target = "type")
    ContentDto toDto(ContentEntity entity);

    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "publishedAt", target = "publishedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "type", target = "contentType")
    ContentEntity toEntity(ContentDto dto);

    List<ContentDto> toDtoList(List<ContentEntity> entityList);

    List<ContentEntity> toEntityList(List<ContentDto> dtoList);

}
