package com.devjsmh.icea.content_manager_service.content.services.validation;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * This configuration register all supported validator at API startup
 * 
 * @author Jhonatan Samuel Martinez Hernandez
 */
@Configuration
public class ValidatorsConfig {

    private final FieldValidatorRegistry registry;

    public ValidatorsConfig(FieldValidatorRegistry registry) {
        this.registry = registry;
    }

    @PostConstruct
    public void registerValidators() {
       
    }

}
