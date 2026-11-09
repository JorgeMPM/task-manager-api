package com.accenture.taskmanager.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.accenture.taskmanager.dto.ErrorResponse;
import com.accenture.taskmanager.dto.LoginRequestDTO;
import com.accenture.taskmanager.dto.LoginResponseDTO;
import com.accenture.taskmanager.dto.PagedResponseDTO;
import com.accenture.taskmanager.dto.TaskRequestDTO;
import com.accenture.taskmanager.dto.TaskResponseDTO;
import com.accenture.taskmanager.model.TaskPriority;
import com.accenture.taskmanager.model.TaskStatus;
import com.accenture.taskmanager.repository.TaskRepository;

/*
Crea la clase de test TaskIntegrationTest con @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT),
@AutoConfigureTestRestTemplate y @ActiveProfiles("test").
Inyecta TestRestTemplate restTemplate y TaskRepository taskRepository con @Autowired
y añade un método @BeforeEach cleanDatabase() que ejecute taskRepository.deleteAll().
No uses mocks.
Añade Javadoc en español a nivel de clase a TaskIntegrationTest, explicando que son tests de integración end-to-end con @SpringBootTest y RANDOM_PORT,
que arrancan la aplicación completa en un servidor real con un puerto aleatorio,
que hacen peticiones HTTP reales con TestRestTemplate contra H2 (perfil test) sin mocks,
y que la base de datos se limpia en @BeforeEach porque con servidor real no hay rollback.
— generado por IA
*/
/**
 * Clase de test de integración para la aplicación TaskManager.
 * Utiliza @SpringBootTest con webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT para arrancar la aplicación completa en un servidor real con un puerto aleatorio.
 * Las pruebas realizan peticiones HTTP reales a los endpoints de la aplicación utilizando TestRestTemplate, contra la base de datos H2 (perfil test) sin utilizar mocks.
 * La base de datos se limpia en @BeforeEach porque con servidor real no hay rollback.
 * Antes de cada test se hace login real contra /auth/login y el token obtenido se envía
 * en la cabecera Authorization: Bearer de cada petición; el test de 401 la omite a propósito.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
public class TaskIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void cleanDatabase() {
        taskRepository.deleteAll();
    }

    /*
    Añade un campo privado HttpHeaders authHeaders y un método @BeforeEach loginAndPrepareHeaders() que haga login real con
    restTemplate.postForEntity("/auth/login", new LoginRequestDTO("admin", "admin123"), LoginResponseDTO.class),
    compruebe con assertEquals que el código de la respuesta es HttpStatus.OK
    y guarde en authHeaders un HttpHeaders nuevo con setBearerAuth(token obtenido de getBody().token()).
    Añade Javadoc en español al método. — generado por IA
    */
    private HttpHeaders authHeaders;

    /**
     * Método que se ejecuta antes de cada test para realizar el login y preparar los headers de autenticación.
     * Hace una petición POST a /auth/login con las credenciales de administrador.
     * Comprueba que la respuesta es HttpStatus.OK y extrae el token de la respuesta.
     * Crea un HttpHeaders nuevo con el token de autenticación Bearer y lo guarda en authHeaders.
     */
    @BeforeEach
    void loginAndPrepareHeaders() {
        ResponseEntity<LoginResponseDTO> loginResponse = restTemplate.postForEntity("/auth/login", new LoginRequestDTO("admin", "admin123"), LoginResponseDTO.class);
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        String token = loginResponse.getBody().token();
        authHeaders = new HttpHeaders();
        authHeaders.setBearerAuth(token);
    }

    /*
    Crea un test @Test getTask_shouldReturn404_whenTaskDoesNotExist con Given/When/Then.
    En el bloque Given indica en un comentario que la base de datos está vacía por el @BeforeEach.
    En el bloque When guarda en ResponseEntity<ErrorResponse> response el resultado de
    restTemplate.getForEntity("/api/v1/tasks/9999", ErrorResponse.class).
    En el bloque Then comprueba con assertEquals que response.getStatusCode() es HttpStatus.NOT_FOUND
    y que response.getBody().error() es "Not Found".
    — generado por IA
    */
    @Test
    void getTask_shouldReturn404_whenTaskDoesNotExist() {
        // Given: La base de datos está vacía por el @BeforeEach
        // When
        ResponseEntity<ErrorResponse> response = restTemplate.exchange("/api/v1/tasks/9999", HttpMethod.GET,
                new HttpEntity<>(null, authHeaders), ErrorResponse.class);
        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody().error());
    }

    /*
    Crea un test @Test createTask_shouldReturn400_whenTitleIsBlank con Given/When/Then.
    En el bloque Given crea un TaskRequestDTO request con
    new TaskRequestDTO("", null, TaskStatus.PENDING, TaskPriority.LOW, null, null, null).
    En el bloque When guarda en ResponseEntity<ErrorResponse> response el resultado de
    restTemplate.postForEntity("/api/v1/tasks", request, ErrorResponse.class).
    En el bloque Then comprueba con assertEquals que response.getStatusCode() es HttpStatus.BAD_REQUEST
    y que response.getBody().message() es "Error de validación".
    — generado por IA
    */
    @Test
    void createTask_shouldReturn400_whenTitleIsBlank() {
        // Given
        TaskRequestDTO request = new TaskRequestDTO("", null, TaskStatus.PENDING, TaskPriority.LOW, null, null, null);
        // When
        ResponseEntity<ErrorResponse> response = restTemplate.exchange("/api/v1/tasks", HttpMethod.POST,
                new HttpEntity<>(request, authHeaders), ErrorResponse.class);
        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error de validación", response.getBody().message());
    }

    /*
    Crea un test @Test getTasksByStatusAndPriority_shouldReturnOnlyMatchingTasks con Given/When/Then.
    En el bloque Given crea con restTemplate.postForEntity("/api/v1/tasks", request, TaskResponseDTO.class) tres tareas:
    new TaskRequestDTO("Pendiente alta", null, TaskStatus.PENDING, TaskPriority.HIGH, LocalDate.now().plusDays(30), null, null),
    new TaskRequestDTO("Pendiente baja", null, TaskStatus.PENDING, TaskPriority.LOW, null, null, null) y
    new TaskRequestDTO("Hecha alta", null, TaskStatus.DONE, TaskPriority.HIGH, LocalDate.now().plusDays(30), null, null).
    En el bloque When guarda en ResponseEntity<PagedResponseDTO<TaskResponseDTO>> response el resultado de
    restTemplate.exchange("/api/v1/tasks/by-status-priority?status=PENDING&priority=HIGH", HttpMethod.GET, null,
    new ParameterizedTypeReference<PagedResponseDTO<TaskResponseDTO>>() {}).
    En el bloque Then comprueba con assertEquals que response.getStatusCode() es HttpStatus.OK,
    que response.getBody().content() tiene tamaño 1
    y que el title() del primer elemento es "Pendiente alta".
    — generado por IA
    */
    @Test
    void getTasksByStatusAndPriority_shouldReturnOnlyMatchingTasks() {
        // Given
       restTemplate.exchange("/api/v1/tasks", HttpMethod.POST,
                new HttpEntity<>(new TaskRequestDTO("Pendiente alta", null, TaskStatus.PENDING, TaskPriority.HIGH, LocalDate.now().plusDays(30), null, null), authHeaders),
                TaskResponseDTO.class);
        restTemplate.exchange("/api/v1/tasks", HttpMethod.POST,
                new HttpEntity<>(new TaskRequestDTO("Pendiente baja", null, TaskStatus.PENDING, TaskPriority.LOW, null, null, null), authHeaders),
                TaskResponseDTO.class);
        restTemplate.exchange("/api/v1/tasks", HttpMethod.POST,
                new HttpEntity<>(new TaskRequestDTO("Hecha alta", null, TaskStatus.DONE, TaskPriority.HIGH, LocalDate.now().plusDays(30), null, null), authHeaders),
                TaskResponseDTO.class);
        // When
        ResponseEntity<PagedResponseDTO<TaskResponseDTO>> response = restTemplate.exchange("/api/v1/tasks/by-status-priority?status=PENDING&priority=HIGH", HttpMethod.GET, new HttpEntity<>(null, authHeaders),
                new ParameterizedTypeReference<PagedResponseDTO<TaskResponseDTO>>() {});
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().content().size());
        assertEquals("Pendiente alta", response.getBody().content().get(0).title());
    }

    /*
    Crea un test @Test taskLifecycle_shouldReturnCorrectStatusAtEachStep con Given/When/Then.
    En el bloque Given crea un TaskRequestDTO createRequest con
    new TaskRequestDTO("Tarea integracion", null, TaskStatus.PENDING, TaskPriority.LOW, null, null, null)
    y un TaskRequestDTO updateRequest con
    new TaskRequestDTO("Tarea actualizada", null, TaskStatus.IN_PROGRESS, TaskPriority.LOW, null, null, null).
    En el bloque When y Then encadena cinco pasos, cada uno con su comprobación assertEquals:
    1) crear con restTemplate.postForEntity("/api/v1/tasks", createRequest, TaskResponseDTO.class): HttpStatus.CREATED,
       y guarda el id con getBody().id() en una variable Long id
    2) obtener con restTemplate.getForEntity("/api/v1/tasks/" + id, TaskResponseDTO.class): HttpStatus.OK y title() "Tarea integracion"
    3) actualizar con restTemplate.exchange("/api/v1/tasks/" + id, HttpMethod.PUT, new HttpEntity<>(updateRequest), TaskResponseDTO.class)
       HttpStatus.OK y status() TaskStatus.IN_PROGRESS
    4) eliminar con restTemplate.exchange("/api/v1/tasks/" + id, HttpMethod.DELETE, null, Void.class): HttpStatus.NO_CONTENT
    5) volver a obtener con restTemplate.getForEntity("/api/v1/tasks/" + id, ErrorResponse.class): HttpStatus.NOT_FOUND.
    — generado por IA
    */
    @Test
    void taskLifecycle_shouldReturnCorrectStatusAtEachStep() {
        // Given
        TaskRequestDTO createRequest = new TaskRequestDTO("Tarea integracion", null, TaskStatus.PENDING, TaskPriority.LOW, null, null, null);
        TaskRequestDTO updateRequest = new TaskRequestDTO("Tarea actualizada", null, TaskStatus.IN_PROGRESS, TaskPriority.LOW, null, null, null);

        // When & Then
        // 1) Crear
        ResponseEntity<TaskResponseDTO> createResponse = restTemplate.exchange("/api/v1/tasks", HttpMethod.POST,
                new HttpEntity<>(createRequest, authHeaders), TaskResponseDTO.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        Long id = createResponse.getBody().id();

        // 2) Obtener
        ResponseEntity<TaskResponseDTO> getResponse = restTemplate.exchange("/api/v1/tasks/" + id, HttpMethod.GET,
                new HttpEntity<>(null, authHeaders), TaskResponseDTO.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("Tarea integracion", getResponse.getBody().title());

        // 3) Actualizar
        ResponseEntity<TaskResponseDTO> updateResponse = restTemplate.exchange("/api/v1/tasks/" + id, HttpMethod.PUT,
                new HttpEntity<>(updateRequest, authHeaders), TaskResponseDTO.class);
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals(TaskStatus.IN_PROGRESS, updateResponse.getBody().status());

        // 4) Eliminar
        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/api/v1/tasks/" + id, HttpMethod.DELETE,
                new HttpEntity<>(null, authHeaders), Void.class);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        // 5) Volver a obtener
        ResponseEntity<ErrorResponse> getAfterDeleteResponse = restTemplate.exchange("/api/v1/tasks/" + id, HttpMethod.GET,
                new HttpEntity<>(null, authHeaders), ErrorResponse.class);
        assertEquals(HttpStatus.NOT_FOUND, getAfterDeleteResponse.getStatusCode());
    }

    /*
    Crea un test @Test getTasks_shouldReturn401_whenNoToken con Given/When/Then.
    En el bloque Given indica en un comentario que la petición se envía deliberadamente sin la cabecera Authorization.
    En el bloque When guarda en ResponseEntity<ErrorResponse> response el resultado de
    restTemplate.getForEntity("/api/v1/tasks", ErrorResponse.class), sin usar authHeaders.
    En el bloque Then comprueba con assertEquals que response.getStatusCode() es HttpStatus.UNAUTHORIZED,
    que response.getBody().status() es 401, que response.getBody().error() es "Unauthorized"
    y que response.getBody().message() es "Se requiere un token JWT válido".
    — generado por IA
    */
    @Test
    void getTasks_shouldReturn401_whenNoToken() {
        // Given: La petición se envía deliberadamente sin la cabecera Authorization
        // When
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity("/api/v1/tasks", ErrorResponse.class);
        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(401, response.getBody().status());
        assertEquals("Unauthorized", response.getBody().error());
        assertEquals("Se requiere un token JWT válido", response.getBody().message());
    }



}