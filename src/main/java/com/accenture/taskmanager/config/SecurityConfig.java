package com.accenture.taskmanager.config;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.accenture.taskmanager.dto.ErrorResponse;
import com.accenture.taskmanager.security.JwtAuthenticationFilter;

import tools.jackson.databind.ObjectMapper;


/**
 * Clase de configuración de seguridad para la aplicación.
 * Esta clase habilita la seguridad web y define la cadena de filtros de seguridad.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad para la aplicación.
     * Permite el acceso sin autenticación al endpoint de login y a la documentación
     * de Swagger/OpenAPI, y exige autenticación para cualquier otra petición.
     * Configura la API como stateless (sin sesión HTTP), desactiva la protección CSRF,
     * registra el {@link JwtAuthenticationFilter} antes de {@link UsernamePasswordAuthenticationFilter}
     * y responde 401 con un {@link ErrorResponse} a las peticiones no autenticadas.
     *
     * @param http el objeto HttpSecurity utilizado para construir la cadena de filtros
     * @param jwtAuthenticationFilter filtro que autentica cada petición a partir del token JWT
     * @param objectMapper mapper JSON de Spring (Jackson 3) para escribir el ErrorResponse
     * @return un SecurityFilterChain configurado
     * @throws Exception si ocurre algún error durante la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter,
                                                   ObjectMapper objectMapper) throws Exception {

        http.authorizeHttpRequests(authz -> authz
                .requestMatchers("/auth/login", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
        );

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.disable());

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) -> {
            HttpStatus status = HttpStatus.UNAUTHORIZED;
            ErrorResponse errorResponse = new ErrorResponse(
                    LocalDateTime.now(),
                    status.value(),
                    status.getReasonPhrase(),
                    "Se requiere un token JWT válido",
                    request.getRequestURI(),
                    null);

            response.setStatus(status.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getOutputStream(), errorResponse);
        }));

        return http.build();
    }
}