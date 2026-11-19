package com.accenture.taskmanager.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.taskmanager.dto.ErrorResponse;
import com.accenture.taskmanager.dto.LoginRequestDTO;
import com.accenture.taskmanager.dto.LoginResponseDTO;
import com.accenture.taskmanager.security.JwtService;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * Controlador para la gestión de autenticación de usuarios.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Inicio de sesión y obtención del token JWT")
public class AuthController {
    
    private final JwtService jwtService;
    // TODO: en producción esto debe venir de base de datos
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    /**
     * Constructor de la clase AuthController.
     *
     * @param jwtService El servicio para la generación de tokens JWT.
     */
    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Maneja la solicitud de inicio de sesión.
     *
     * @param loginRequest El objeto que contiene las credenciales de inicio de sesión.
     * @param request La solicitud HTTP entrante.
     * @return Una respuesta con el token JWT si las credenciales son correctas, o un error si no lo son.
     */
    @SecurityRequirements
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest, HttpServletRequest request) {
        if (ADMIN_USERNAME.equals(loginRequest.username()) && ADMIN_PASSWORD.equals(loginRequest.password())) {
            String token = jwtService.generateToken(loginRequest.username());
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } else {
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            ErrorResponse errorResponse = new ErrorResponse(
                    LocalDateTime.now(),
                    status.value(),
                    status.getReasonPhrase(),
                    "Credenciales incorrectas",
                    request.getRequestURI(),
                    null);
            return ResponseEntity.status(status).body(errorResponse);
        }
    }

}