package com.accenture.taskmanager.mapper;

import org.springframework.stereotype.Component;

import com.accenture.taskmanager.dto.CategoryRequestDTO;
import com.accenture.taskmanager.dto.CategoryResponseDTO;
import com.accenture.taskmanager.model.Category;

/**
 * Mapper para convertir objetos de tipo Category en objetos de tipo CategoryResponseDTO y viceversa.
 * 
 */
@Component
public class CategoryMapper {

    /**
     * Convierte una entidad Category en un objeto CategoryResponseDTO.
     *
     * @param category la entidad Category a convertir.
     * @return un objeto CategoryResponseDTO con los datos de la categoría.
     */
    public CategoryResponseDTO toResponseDTO(Category category) {
        return new CategoryResponseDTO(category.getId(), category.getName(), category.getDescription());
    }

    /**
     * Convierte un objeto CategoryRequestDTO en una nueva entidad Category.
     *
     * @param categoryRequestDTO el objeto CategoryRequestDTO a convertir.
     * @return una nueva entidad Category con los datos de la solicitud.
     */
    public Category toEntity(CategoryRequestDTO categoryRequestDTO) {
        Category category = new Category();
        category.setName(categoryRequestDTO.name());
        category.setDescription(categoryRequestDTO.description());
        return category;
    }

}
