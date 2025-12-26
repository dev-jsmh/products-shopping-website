package com.devjsmh.icea.content_manager_service.content.services.validation;

import org.springframework.context.annotation.Configuration;

import com.devjsmh.icea.content_manager_service.content.services.validation.types.DateTimeField;
import com.devjsmh.icea.content_manager_service.content.services.validation.types.MediaField;
import com.devjsmh.icea.content_manager_service.content.services.validation.types.RichTextField;
import com.devjsmh.icea.content_manager_service.content.services.validation.types.TextField;

import jakarta.annotation.PostConstruct;

/**
 * This configuration register all supported   at API startup
 * 
 * @author Jhonatan Samuel Martinez Hernandez
 */
@Configuration
public class  ContentTypeFieldsConfig {

    private final ContentTypeFieldRegistry registry;
    private final TextField textField;
    private final MediaField mediaField;
    private final RichTextField richTextField;
    private final DateTimeField dateTimeField;

    public  ContentTypeFieldsConfig(
            ContentTypeFieldRegistry registry,
            TextField textField,
            MediaField mediaField,
            RichTextField richTextField,
            DateTimeField dateTimeField) {
        this.registry = registry;
        this.textField  = textField ;
        this.mediaField  = mediaField ;
        this.richTextField  = richTextField ;
        this.dateTimeField  = dateTimeField ;
    }

    @PostConstruct
    public void registerFieldTypes() {
        registry.add("text", textField );
        registry.add("media", mediaField );
        registry.add("richtext", richTextField );
        registry.add("datetime", dateTimeField );
    }

}
