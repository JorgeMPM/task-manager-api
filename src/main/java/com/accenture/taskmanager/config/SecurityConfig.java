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


/*
 Crea la clase SecurityConfig anotada con @Configuration y @EnableWebSecurity, añade Javadoc a nivel de clase en español.
 Declara un método @Bean securityFilterChain(HttpSecurity http) que devuelva un SecurityFilterChain construido con http.build(), con Javadoc, sin configurar todavía ninguna regla de acceso. Usa la API de Spring Security 7 (Spring Boot 4), sin WebSecurityConfigurerAdapter. — generado por IA
*/
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

        /*
        Dentro del bean securityFilterChain, configura authorizeHttpRequests con una lambda: 
        permite el acceso sin autenticación (permitAll) a las rutas /auth/login, /swagger-ui.html, /swagger-ui/** y /v3/api-docs/**, 
        y exige autenticación (authenticated) para cualquier otra petición con anyRequest(). 
        No añadas httpBasic(), formLogin() ni usuarios en memoria. — generado por IA
        */
        http.authorizeHttpRequests(authz -> authz
                .requestMatchers("/auth/login", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
        );

        /*
        Dentro del bean securityFilterChain, configura sessionManagement 
        con una lambda para usar SessionCreationPolicy.STATELESS y desactiva la protección CSRF con csrf(csrf -> csrf.disable()), 
        usando la sintaxis de lambdas de Spring Security 7. — generado por IA
        */
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.disable());

        /*
        Registra jwtAuthenticationFilter en la cadena antes de UsernamePasswordAuthenticationFilter con http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).
        No modifiques las reglas de rutas, la política de sesión ni la configuración de CSRF. — generado por IA
        */
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        /*
        Configura exceptionHandling con una lambda y un authenticationEntryPoint propio (request, response, authException) que responda 401 Unauthorized
        con un ErrorResponse construido igual que en GlobalExceptionHandler: LocalDateTime.now(), HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
        el mensaje "Se requiere un token JWT válido", request.getRequestURI() y null.
        Antes de escribir, fija response.setStatus(HttpStatus.UNAUTHORIZED.value()), el content type MediaType.APPLICATION_JSON_VALUE y la codificación UTF-8,
        y escribe el JSON con objectMapper.writeValue(response.getOutputStream(), errorResponse). No uses authException.getMessage() en la respuesta. — generado por IA
        */
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