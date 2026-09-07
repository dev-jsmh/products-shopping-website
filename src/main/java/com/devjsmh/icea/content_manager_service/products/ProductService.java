package com.devjsmh.icea.content_manager_service.products;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.devjsmh.icea.content_manager_service.core.Exceptions.NoSuchEntityExistsException;

@Service
public class ProductService {

    private final IProductRepository productRepository;

    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Gets all products from the database
     * 
     * @return a list of entries
     * @version 1
     */
    public List<ProductEntity> getAllV1() {
        return this.productRepository.findAll();
    }

    /**
     * Gets a product by id number
     * @param id
     * @return the found product
     * @version 1
     */
    public ProductEntity getByIdV1(Long id) {

        Optional<ProductEntity> OptProduct = this.productRepository.findById(id);

        if (OptProduct.isPresent()) {
            return OptProduct.get();
        }

        throw new NoSuchEntityExistsException("Product", "id", id.toString());
    }
}
