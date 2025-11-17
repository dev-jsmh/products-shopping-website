package com.devjsmh.icea.content_manager_service.contentType;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devjsmh.icea.content_manager_service.contentType.dtos.ContentTypeDto;
import com.devjsmh.icea.content_manager_service.contentType.mappers.IContentTypeMapper;

/**
 * @author Jhonatan Samuel Martinez
 */

@RestController
@RequestMapping("/api")
public class ContentTypeController {

    private final ContentTypeService contentTypeService;
    private final IContentTypeMapper contentTypeMapper;

    // inject dependencies
    public ContentTypeController(ContentTypeService contentTypeService, IContentTypeMapper mapper) {
        this.contentTypeService = contentTypeService;
        this.contentTypeMapper = mapper;
    }

    @GetMapping("/v1/content-types")
    public ResponseEntity<List<ContentTypeDto>> getAllV1() {
        // returns all the saved records
        List<ContentTypeEntity> entities = this.contentTypeService.getAllV1();
        List<ContentTypeDto> dtos = this.contentTypeMapper.toDtoList(entities);
        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/v1/content-types/{slug}")
    public ResponseEntity<ContentTypeDto> getBySlugV1(@PathVariable("slug") String slug) {
        ContentTypeEntity entity = this.contentTypeService.getBySlugV1(slug);
        ContentTypeDto dto = this.contentTypeMapper.toDto(entity);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping("/v1/content-types")
    public ResponseEntity<ContentTypeDto> saveV1(@RequestBody ContentTypeDto request) {
        ContentTypeEntity tempEntity = this.contentTypeMapper.toEntity(request);
        ContentTypeEntity entity = this.contentTypeService.saveEntityV1(tempEntity);
        ContentTypeDto dto = this.contentTypeMapper.toDto(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/v1/content-types/{slug}")
    public ResponseEntity<ContentTypeDto> updateBySlugV1(@RequestBody ContentTypeDto request, @PathVariable("slug") String slug) {
        ContentTypeEntity tempEntity = this.contentTypeMapper.toEntity(request);
        ContentTypeEntity entity = this.contentTypeService.updateBySlugV1(tempEntity, slug);
        ContentTypeDto dto = this.contentTypeMapper.toDto(entity);
        return ResponseEntity.ok().body(dto);

    }

    @DeleteMapping("/v1/content-types/{slug}")
    public ResponseEntity<Void> deletebySlugV1(@PathVariable("slug") String slug) {
        this.contentTypeService.deleteBySlugV1(slug);
        return ResponseEntity.noContent().build();
    }

}
