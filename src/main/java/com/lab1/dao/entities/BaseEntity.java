package com.lab1.dao.entities;

import com.lab1.exceptions.entity.EntityCreatorException;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    private String createdBy;

    private LocalDateTime createdAt;

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    public void defineCreatedByAndCreatedAt() {
        createdAt = LocalDateTime.now();
        createdBy = findUserName();
    }

    private String findUserName() {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        if (principal == null) throw new EntityCreatorException("Невозможно определить владельца организаций");

        return principal.getName();
    }
}