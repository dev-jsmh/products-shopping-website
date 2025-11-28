package com.devjsmh.icea.content_manager_service.content;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devjsmh.icea.content_manager_service.content.dtos.ContentDetailedDto;
import com.devjsmh.icea.content_manager_service.content.dtos.ContentDto;
import com.devjsmh.icea.content_manager_service.content.mappers.IContentMapper;
import com.devjsmh.icea.content_manager_service.content.mappers.IContentWithContentTypeSummaryMapper;

/**
 * 
 * Handles all requests related to content entity and triggers the
 * correct methods from the service layer to perform an action
 * 
 * @author Jhonatan Samuel Martinez
 */
@RestController
@RequestMapping("/api")
public class ContentController {

    private final ContentService contentService;
    private final IContentMapper contentMapper;
    private final IContentWithContentTypeSummaryMapper contentWithContentTypeMapper;

    // inject dependencies
    public ContentController(
            ContentService contentService,
            IContentMapper contentMapper,
            IContentWithContentTypeSummaryMapper contentWithContentTypeMapper) {
        this.contentService = contentService;
        this.contentMapper = contentMapper;
        this.contentWithContentTypeMapper = contentWithContentTypeMapper;
    }

    @GetMapping("/v1/contents/{slug}")
    public ResponseEntity<List<ContentDetailedDto>> getAllV1(@PathVariable("slug") String contentTypeSlug) {
        List<ContentEntity> response = this.contentService.getAllV1(contentTypeSlug);
        List<ContentDetailedDto> dtoList = this.contentWithContentTypeMapper.toDtoList(response);
        return ResponseEntity.ok().body(dtoList);
    }

    /**
     * Saved a new content entry of a type
     * 
     * @param slug    friendly url of the content type that need to be created
     * @param request body with fields of the new content entry
     * @return content entries of the specified type
     */
    @PostMapping("/v1/contents/{slug}")
    public ResponseEntity<ContentDetailedDto> createV1(@PathVariable("slug") String contentTypeSlug,
            @RequestBody ContentDto request) {
        ContentEntity tempEntity = this.contentMapper.toEntity(request);
        ContentEntity response = this.contentService.createV1(contentTypeSlug, tempEntity);
        // use a different mapper to map the response to a dto with a summaries content
        // type
        ContentDetailedDto dto = this.contentWithContentTypeMapper.toDto(response);
        return ResponseEntity.ok().body(dto);
    }

    /**
     * Gets a content entry by type and content id
     * 
     * @param slug from the content-type
     * @param id   from the content entry
     * 
     * @return the found content entry
     */
    @GetMapping("/v1/contents/{slug}/{id}")
    public ResponseEntity<ContentDetailedDto> getByTypeAndIdV1(
            @PathVariable("slug") String contentTypeSlug,
            @PathVariable("id") Long contentId) {
        ContentEntity entity = this.contentService.getByTypeAndIdV1(contentTypeSlug, contentId);
        ContentDetailedDto dto = this.contentWithContentTypeMapper.toDto(entity);
        return ResponseEntity.ok().body(dto);
    }
}
