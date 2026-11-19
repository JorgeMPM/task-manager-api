package com.accenture.taskmanager.mapper;

import org.springframework.stereotype.Component;

import com.accenture.taskmanager.dto.TagRequestDTO;
import com.accenture.taskmanager.dto.TagResponseDTO;
import com.accenture.taskmanager.model.Tag;

/**
 * Mapper para convertir objetos de tipo Tag en objetos de tipo TagResponseDTO y viceversa.
 * Esta clase se encarga de mapear los datos entre las entidades Tag y los DTOs correspondientes.
 */
@Component
public class TagMapper {

    /**
     * Convierte una entidad Tag en un objeto TagResponseDTO.
     *
     * @param tag la entidad Tag a convertir.
     * @return un objeto TagResponseDTO con los datos de la etiqueta, o null si el tag es null.
     */
    public TagResponseDTO toResponseDTO(Tag tag) {
        if (tag == null) {
            return null;
        }
        return new TagResponseDTO(tag.getId(), tag.getName());
    }

    /**
    * Convierte un objeto TagRequestDTO en una nueva entidad Tag.
    *
    * @param tagRequestDTO el objeto TagRequestDTO a convertir.
    * @return una nueva entidad Tag con el nombre de la etiqueta, o null si el DTO es null.
    */
    public Tag toEntity(TagRequestDTO tagRequestDTO) {
        if (tagRequestDTO == null) {
            return null;
        }   
    
        Tag tag = new Tag();
        tag.setName(tagRequestDTO.name());
        return tag;
    }

}  
