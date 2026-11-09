package com.accenture.taskmanager.service;

import java.util.NoSuchElementException;

import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import com.accenture.taskmanager.dto.CategoryRequestDTO;
import com.accenture.taskmanager.dto.CategoryResponseDTO;
import com.accenture.taskmanager.dto.PagedResponseDTO;

/*
 Crea la interfaz CategoryService con el CRUD básico de categorías usando DTOs:
 findAll paginado que devuelve PagedResponseDTO, findById, create, update y delete.
 Añade Javadoc en español. — generado por IA
*/
/**
 * Interfaz que define los servicios para la gestión de categorías.
 */
public interface CategoryService {

    /**
     * Obtiene todas las categorías con paginación.
     *
     * @param pageable objeto que contiene la información de paginación
     * @return una página de categorías
     */
    PagedResponseDTO<CategoryResponseDTO> findAll(Pageable pageable);

    /**
     * Obtiene una categoría por su ID.
     *
     * @param id el ID de la categoría a buscar
     * @return la categoría encontrada
     * @throws NoSuchElementException si no existe una categoría con ese id
     */
    CategoryResponseDTO findById(Long id);

    /**
     * Crea una nueva categoría.
     *
     * @param categoryRequestDTO los datos de la nueva categoría
     * @return la categoría creada
     */
    CategoryResponseDTO create(CategoryRequestDTO categoryRequestDTO);

    /**
     * Actualiza una categoría existente.
     *
     * @param id el ID de la categoría a actualizar
     * @param categoryRequestDTO los nuevos datos de la categoría
     * @return la categoría actualizada
     * @throws NoSuchElementException si no existe una categoría con ese id
     */
    CategoryResponseDTO update(Long id, CategoryRequestDTO categoryRequestDTO);

    /**
     * Elimina una categoría por su ID.
     *
     * @param id el ID de la categoría a eliminar
     * @throws NoSuchElementException si no existe una categoría con ese id
     * @throws ResponseStatusException si la categoría tiene tareas asociadas
     */
    void delete(Long id);
}
