package com.accenture.taskmanager.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.accenture.taskmanager.dto.TagRequestDTO;
import com.accenture.taskmanager.dto.TagResponseDTO;
import com.accenture.taskmanager.mapper.TagMapper;
import com.accenture.taskmanager.model.Tag;
import com.accenture.taskmanager.repository.TagRepository;

/* Crea la clase TagServiceImpl que implemente TagService, anotada con @Service.
Añade los campos private final TagRepository tagRepository y private final TagMapper tagMapper inyectados por constructor.
Añade javadoc en español a la clase y al constructor con @param. — generado por IA */
/**
 * Implementación del servicio para la gestión de etiquetas en el sistema de gestión de tareas.
 * Esta clase implementa la interfaz TagService y proporciona la lógica de negocio para las operaciones relacionadas con las etiquetas.
 */
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    /**
     * Constructor de la clase TagServiceImpl.
     *
     * @param tagRepository El repositorio de etiquetas.
     * @param tagMapper El mapeador de etiquetas.
     */
    public TagServiceImpl(TagRepository tagRepository, TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    /* Implementa findAll anotado con @Transactional(readOnly = true) y @Override: obtiene todas las etiquetas con tagRepository.findAll() y las convierte a TagResponseDTO con tagMapper.toResponseDTO.
    Añade javadoc en español con @return. — generado por IA */
    /**
     * Devuelve una lista con todas las etiquetas.
     *
     * @return Una lista de {@code TagResponseDTO} con todas las etiquetas.
     */
    @Override
    @Transactional(readOnly = true)
    public List<TagResponseDTO> findAll() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toResponseDTO)
                .toList();
    }

    /* Implementa findById anotado con @Transactional(readOnly = true) y @Override: busca la etiqueta con tagRepository.findById y la convierte con tagMapper.toResponseDTO.
    Si no existe lanza ResponseStatusException con HttpStatus.NOT_FOUND y el mensaje "Etiqueta no encontrada con id: " seguido del id.
    Añade javadoc en español con @param, @return y @throws. — generado por IA */
    /**
     * Devuelve la etiqueta correspondiente al ID proporcionado.
     *
     * @param id El ID de la etiqueta a buscar.
     * @return El {@code TagResponseDTO} correspondiente a la etiqueta.
     * @throws NoSuchElementException Si no se encuentra la etiqueta.
     */
    @Override
    @Transactional(readOnly = true)
    public TagResponseDTO findById(Long id) {
        return tagRepository.findById(id)
                .map(tagMapper::toResponseDTO)
   
                /* Si no existe, lanza new NoSuchElementException("Tag con id " + id + " no encontrado")
                   con orElseThrow — generado por IA */
                .orElseThrow(() -> new NoSuchElementException("Tag con id " + id + " no encontrado"));
    }

    /* Implementa create anotado con @Transactional y @Override: convierte el TagRequestDTO en entidad con tagMapper.toEntity, la guarda con tagRepository.save y devuelve el resultado convertido con tagMapper.toResponseDTO.
    Añade javadoc en español con @param y @return. — generado por IA */
    /**
     * Crea una nueva etiqueta a partir de los datos proporcionados.
     *
     * @param tagRequestDTO Los datos para crear la nueva etiqueta.
     * @return El {@code TagResponseDTO} de la nueva etiqueta creada.
     */
    @Override
    @Transactional
    public TagResponseDTO create(TagRequestDTO tagRequestDTO) {
        Tag tag = tagMapper.toEntity(tagRequestDTO);
        return tagMapper.toResponseDTO(tagRepository.save(tag));
    }

}   