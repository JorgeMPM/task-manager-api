package com.accenture.taskmanager.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.taskmanager.dto.TagRequestDTO;
import com.accenture.taskmanager.dto.TagResponseDTO;
import com.accenture.taskmanager.service.TagService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.accenture.taskmanager.dto.ErrorResponse;

/**
 * Controlador REST para la gestión de etiquetas en el sistema de gestión de
 * tareas.
 * Esta clase maneja las solicitudes HTTP relacionadas con las etiquetas y
 * delega la lógica de negocio al servicio correspondiente.
 */
@Tag(name = "Etiquetas", description = "Operaciones relacionadas con la gestión de etiquetas")
@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagService tagService;

    /**
     * Constructor de la clase TagController.
     *
     * @param tagService El servicio de etiquetas.
     */
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Devuelve una lista con todas las etiquetas.
     *
     * @return respuesta con la lista de etiquetas {@code TagResponseDTO} y código
     *         HTTP 200 OK.
     */
    @Operation(summary = "Obtiene todas las etiquetas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Etiquetas obtenidas correctamente")
    })
    @GetMapping
    public ResponseEntity<List<TagResponseDTO>> getAllTags() {
        return ResponseEntity.ok(tagService.findAll());
    }

    /**
     * Devuelve la etiqueta correspondiente al ID proporcionado.
     *
     * @param id El ID de la etiqueta a buscar.
     * @return respuesta con el {@code TagResponseDTO} correspondiente y código HTTP
     *         200 OK.
     */
    @Operation(summary = "Obtiene una etiqueta por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Etiqueta obtenida correctamente"),
            @ApiResponse(responseCode = "404", description = "Etiqueta no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<TagResponseDTO> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.findById(id));
    }

    /**
     * Crea una nueva etiqueta a partir de los datos proporcionados.
     *
     * @param tagRequestDTO Los datos para crear la nueva etiqueta.
     * @return respuesta con el {@code TagResponseDTO} de la nueva etiqueta creada y
     *         código HTTP 201 CREATED.
     */
    @Operation(summary = "Crea una nueva etiqueta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Etiqueta creada correctamente")
    })
    @PostMapping
    public ResponseEntity<TagResponseDTO> createTag(@RequestBody TagRequestDTO tagRequestDTO) {
        return ResponseEntity.status(201).body(tagService.create(tagRequestDTO));
    }
}