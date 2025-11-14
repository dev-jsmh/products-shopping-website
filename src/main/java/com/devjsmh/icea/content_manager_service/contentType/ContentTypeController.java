package com.devjsmh.icea.content_manager_service.contentType;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Jhonatan Samuel Martinez
 */

@RestController
@RequestMapping("/api")
public class ContentTypeController {

    private final ContentTypeService contentTypeService;

    // inject dependencies
    public ContentTypeController(ContentTypeService contentTypeService) {
        this.contentTypeService = contentTypeService;
    }

    @GetMapping("/v1/content-types")
    public List<ContentTypeEntity> getAllV1() {
        // returns all the saved records
        return this.contentTypeService.getAllV1();
    }

    @GetMapping("/v1/content-types/{id}")
    public ContentTypeEntity getByIdV1(@PathVariable Long id) {
        return this.contentTypeService.getByIdV1(id);
    }

    @PostMapping("/v1/content-types")
    public ContentTypeEntity saveV1(@RequestBody ContentTypeEntity type) {

        return this.contentTypeService.saveEntityV1(type);
    }

    @PutMapping("/v1/content-types/{id}")
    public ContentTypeEntity updateByIdV1(@RequestBody ContentTypeEntity request, @PathVariable Long id) {
        return this.contentTypeService.updateByIdV1(request, id);
    }

    @DeleteMapping("/v1/content-types/{id}")
    public void deletebyIdV1(@PathVariable Long id){
        this.contentTypeService.deleteByIdV1(id);
    }

}
