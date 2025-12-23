package com.devjsmh.icea.content_manager_service.content.services.validation;

import org.springframework.context.annotation.Configuration;

import com.devjsmh.icea.content_manager_service.content.services.validation.types.DateTimeFieldValidator;
import com.devjsmh.icea.content_manager_service.content.services.validation.types.MediaFieldValidator;
import com.devjsmh.icea.content_manager_service.content.services.validation.types.RichTextFieldValidator;
import com.devjsmh.icea.content_manager_service.content.services.validation.types.TextFieldValidator;

import jakarta.annotation.PostConstruct;

/**
 * This configuration register all supported validator at API startup
 * 
 * @author Jhonatan Samuel Martinez Hernandez
 */
@Configuration
public class ValidatorsConfig {

    private final FieldValidatorRegistry registry;
    private final TextFieldValidator textFieldValidator;
    private final MediaFieldValidator mediaFieldValidator;
    private final RichTextFieldValidator richTextFieldValidator;
    private final DateTimeFieldValidator dateTimeFieldValidator;

    public ValidatorsConfig(
            FieldValidatorRegistry registry,
            TextFieldValidator textFieldValidator,
            MediaFieldValidator mediaFieldValidator,
            RichTextFieldValidator richTextFieldValidator,
            DateTimeFieldValidator dateTimeFieldValidator) {
        this.registry = registry;
        this.textFieldValidator = textFieldValidator;
        this.mediaFieldValidator = mediaFieldValidator;
        this.richTextFieldValidator = richTextFieldValidator;
        this.dateTimeFieldValidator = dateTimeFieldValidator;
    }

    @PostConstruct
    public void registerValidators() {
        registry.add("text", textFieldValidator);
        registry.add("media", mediaFieldValidator);
        registry.add("richtext", richTextFieldValidator);
        registry.add("datetime", dateTimeFieldValidator);
    }

}
