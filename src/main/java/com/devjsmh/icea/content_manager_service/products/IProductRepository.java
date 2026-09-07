package com.devjsmh.icea.content_manager_service.products;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<ProductEntity, Long> {

    @EntityGraph(attributePaths = "images")
    Optional<ProductEntity> findById(Long id);
}
