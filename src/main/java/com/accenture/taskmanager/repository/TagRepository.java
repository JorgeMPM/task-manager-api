package com.accenture.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.accenture.taskmanager.model.Tag;

/* Crea la interfaz TagRepository que extienda JpaRepository<Tag, Long> para el acceso a datos de la entidad Tag.
Añade javadoc en español a nivel de interfaz. — generado por IA */
/**
 * Repositorio para la entidad Tag en el sistema de gestión de tareas.
 * Esta interfaz extiende JpaRepository para proporcionar operaciones CRUD y de consulta para la entidad Tag.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
}
