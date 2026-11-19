package com.accenture.taskmanager.service;


import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.accenture.taskmanager.dto.CategoryRequestDTO;
import com.accenture.taskmanager.dto.CategoryResponseDTO;
import com.accenture.taskmanager.dto.PagedResponseDTO;
import com.accenture.taskmanager.mapper.CategoryMapper;
import com.accenture.taskmanager.model.Category;
import com.accenture.taskmanager.repository.CategoryRepository;
import com.accenture.taskmanager.repository.TaskRepository;



/**
 * Implementación del servicio de gestión de categorías.
 * Esta clase proporciona la lógica de negocio para las operaciones relacionadas con las categorías,
 * incluyendo la creación, actualización, eliminación y recuperación de categorías.
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    
    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;
    private final CategoryMapper categoryMapper;

    /**
     * Constructor de la clase CategoryServiceImpl.
     *
     * @param categoryRepository el repositorio de categorías.
     * @param taskRepository el repositorio de tareas.
     * @param categoryMapper el mapper para convertir entre entidades y DTOs.
     */
    public CategoryServiceImpl(CategoryRepository categoryRepository, TaskRepository taskRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.taskRepository = taskRepository;
        this.categoryMapper = categoryMapper;
    }

    /**
     * Obtiene todas las categorías con paginación.
     *
     * @param pageable objeto que contiene la información de paginación
     * @return una página de categorías
     */
    @Override
    @Transactional(readOnly = true)
    public PagedResponseDTO<CategoryResponseDTO> findAll(Pageable pageable) {
        Page<Category> page = categoryRepository.findAll(pageable);
        return new PagedResponseDTO<>(
                page.getContent().stream().map(categoryMapper::toResponseDTO).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    /**
     * Obtiene una categoría por su ID o lanza una excepción si no se encuentra.
     *
     * @param id el ID de la categoría a buscar
     * @return la categoría encontrada
     * @throws NoSuchElementException si no se encuentra la categoría
     */
    private Category getCategoryOrThrow(Long id) {
        
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoría con id " + id + " no encontrada"));
    }

    /**
     * Obtiene una categoría por su ID.
     *
     * @param id el ID de la categoría a buscar
     * @return la categoría encontrada
     * @throws NoSuchElementException si no se encuentra la categoría
     */
    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDTO findById(Long id) {
        Category category = getCategoryOrThrow(id);
        return categoryMapper.toResponseDTO(category);
    }

    /**
     * Crea una nueva categoría.
     *
     * @param categoryRequestDTO los datos de la nueva categoría
     * @return la categoría creada
     */
    @Override
    @Transactional
    public CategoryResponseDTO create(CategoryRequestDTO categoryRequestDTO) {
        Category category = categoryMapper.toEntity(categoryRequestDTO);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toResponseDTO(savedCategory);
    }

    /**
     * Actualiza una categoría existente.
     *
     * @param id el ID de la categoría a actualizar
     * @param categoryRequestDTO los nuevos datos de la categoría
     * @return la categoría actualizada
     */
    @Override
    @Transactional
    public CategoryResponseDTO update(Long id, CategoryRequestDTO categoryRequestDTO) {
        Category existingCategory = getCategoryOrThrow(id);
        existingCategory.setName(categoryRequestDTO.name());
        existingCategory.setDescription(categoryRequestDTO.description());
        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.toResponseDTO(updatedCategory);
    }

    /**
     * Elimina una categoría por su ID.
     *
     * @param id el ID de la categoría a eliminar
     * @throws NoSuchElementException si no se encuentra la categoría
     * @throws ResponseStatusException si la categoría tiene tareas asociadas
     */
    @Override
    @Transactional
    public void delete(Long id) {
        getCategoryOrThrow(id); // Lanza 404 si no existe
        if (taskRepository.existsByCategoryId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La categoría con id " + id + " tiene tareas asociadas");
        }
        categoryRepository.deleteById(id);
    }

 }

   