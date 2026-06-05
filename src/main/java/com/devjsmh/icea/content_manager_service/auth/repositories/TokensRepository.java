package com.devjsmh.icea.content_manager_service.auth.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devjsmh.icea.content_manager_service.auth.entities.TokenEntity;

@Repository
public interface TokensRepository extends JpaRepository<TokenEntity, Long> {

    Optional<TokenEntity> findByToken(String searchedToken);

    List<TokenEntity> findAllByUserIdAndIsRevokedFalse(Long userId);
}
