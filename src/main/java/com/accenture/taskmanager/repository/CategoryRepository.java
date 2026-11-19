package com.accenture.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.accenture.taskmanager.model.Category;

/**
 * Repositorio para la entidad {@code Category}.
 * Proporciona métodos CRUD y de consulta para la entidad {@code Category} mediante la extensión de {@code JpaRepository}.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    
}

