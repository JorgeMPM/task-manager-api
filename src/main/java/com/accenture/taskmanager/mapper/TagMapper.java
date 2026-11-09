package com.accenture.taskmanager.mapper;

import org.springframework.stereotype.Component;

import com.accenture.taskmanager.dto.TagRequestDTO;
import com.accenture.taskmanager.dto.TagResponseDTO;
import com.accenture.taskmanager.model.Tag;

/* Crea una clase TagMapper para convertir objetos de tipo Tag en objetos de tipo TagResponseDTO y TagRequestDTO a la entidad Tag.
Añade la anotación @Component. Añade javadoc en español a nivel de clase. — generado por IA*/
/**
 * Mapper para convertir objetos de tipo Tag en objetos de tipo TagResponseDTO y viceversa.
 * Esta clase se encarga de mapear los datos entre las entidades Tag y los DTOs correspondientes.
 */
@Component
public class TagMapper {

    /* Crea el método toResponseDTO que recibe un Tag y devuelve un TagResponseDTO con su id y name.
    Si el Tag es null devuelve null. Añade javadoc en español con @param y @return. — generado por IA */
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

    /* Crea el método toEntity que recibe un TagRequestDTO y devuelve una entidad Tag con el name del DTO, sin asignar id ni tasks.
    Si el DTO es null devuelve null. Añade javadoc en español con @param y @return. — generado por IA */
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
