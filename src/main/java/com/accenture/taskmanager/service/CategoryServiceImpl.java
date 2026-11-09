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


// Crea la clase CategoryServiceImpl anotada con @Service que implementa la interfaz CategoryService.
//Implementa todos los metodos de la interfaz usando categoryRepository y categoryMapper.
// Declara tres campos private final: CategoryRepository categoryRepository, TaskRepository taskRepository y CategoryMapper categoryMapper.
// Añade un constructor que reciba los tres y los asigne (inyección por constructor, sin @Autowired).
// Añade Javadoc en la clase y en el constructor, en español. — generado por IA

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

    /*
    Implementa findAll(Pageable pageable) con Override y Transactional(readOnly = true).
    Convierte el resultado de categoryRepository.findAll(pageable) con page.map(categoryMapper::toResponseDTO)
    y devuelve un nuevo PagedResponseDTO con content, number, size, totalElements, totalPages y isLast de la página.
    Añade Javadoc en español. — generado por IA
    */
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

    /*
    Crea un método privado getCategoryOrThrow(Long id) que devuelva la Category buscada con categoryRepository.findById(id).
    Si no existe, lanza con orElseThrow una ResponseStatusException con HttpStatus.NOT_FOUND
    y el mensaje "Categoría no encontrada con id: " + id.
    Añade Javadoc en español. — generado por IA
    */
    /**
     * Obtiene una categoría por su ID o lanza una excepción si no se encuentra.
     *
     * @param id el ID de la categoría a buscar
     * @return la categoría encontrada
     * @throws NoSuchElementException si no se encuentra la categoría
     */
    private Category getCategoryOrThrow(Long id) {
        
        /* Devuelve la categoría con categoryRepository.findById y, si no existe, lanza
           new NoSuchElementException("Categoría con id " + id + " no encontrada")
           con orElseThrow — generado por IA */
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoría con id " + id + " no encontrada"));
    }

    /*
    Implementa findById(Long id) con Override y Transactional(readOnly = true).
    Obtén la Category con getCategoryOrThrow(id) y devuélvela convertida con categoryMapper.toResponseDTO.
    Añade Javadoc en español. — generado por IA
    */
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

    /*
    Implementa create(CategoryRequestDTO dto) con Override y Transactional.
    Convierte el dto con categoryMapper.toEntity(dto), guárdalo con categoryRepository.save
    y devuelve la entidad guardada convertida con categoryMapper.toResponseDTO.
    Añade Javadoc en español. — generado por IA
    */
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

    /*
    Implementa update(Long id, CategoryRequestDTO dto) con Override y Transactional.
    Obtén la Category existente con getCategoryOrThrow(id); no crees una entidad nueva ni uses toEntity.
    Modifica solo name y description con los valores del dto, sin tocar tasks.
    Guarda con categoryRepository.save y devuelve el resultado con categoryMapper.toResponseDTO.
    Añade Javadoc en español. — generado por IA
    */
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

    /*
    Implementa delete(Long id) con Override y Transactional, sin valor de retorno.
    Llama primero a getCategoryOrThrow(id) para lanzar 404 si no existe.
    Si taskRepository.existsByCategoryId(id) es true, lanza una ResponseStatusException con HttpStatus.CONFLICT
    y el mensaje "La categoría con id " + id + " tiene tareas asociadas".
    Si no, bórrala con categoryRepository.deleteById(id).
    Añade Javadoc en español. — generado por IA
    */
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

   