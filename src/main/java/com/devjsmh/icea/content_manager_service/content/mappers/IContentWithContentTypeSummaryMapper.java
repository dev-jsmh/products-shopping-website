package com.devjsmh.icea.content_manager_service.content.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.devjsmh.icea.content_manager_service.content.ContentEntity;
import com.devjsmh.icea.content_manager_service.content.dtos.ContentDetailedDto;
import com.devjsmh.icea.content_manager_service.contentType.mappers.IContentTypeSummaryMapper;

@Mapper(componentModel = "spring", uses = IContentTypeSummaryMapper.class)
public interface IContentWithContentTypeSummaryMapper {

    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "updatedAt", target = "updatedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "publishedAt", target = "publishedAt", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(source = "contentType", target = "type")
    ContentDetailedDto toDto(ContentEntity entity);

    List<ContentDetailedDto> toDtoList(List<ContentEntity> listEntity);

}
