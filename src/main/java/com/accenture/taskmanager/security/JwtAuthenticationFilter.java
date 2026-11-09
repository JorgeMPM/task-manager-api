package com.accenture.taskmanager.security;

import java.util.Collections;
import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 Crea la clase JwtAuthenticationFilter anotada con @Component que extienda OncePerRequestFilter, con Javadoc de clase en español, e inyecta JwtService por constructor.
 Implementa doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) con este flujo:
 1) lee la cabecera Authorization; si es nula o no empieza por "Bearer ", llama a filterChain.doFilter(request, response) y termina con return
 2) extrae el token quitando el prefijo "Bearer ";
 3) si jwtService.isTokenValid(token) es true y SecurityContextHolder.getContext().getAuthentication() es null, obtiene el username con jwtService.extractUsername(token), crea un UsernamePasswordAuthenticationToken(username, null, Collections.emptyList()), añade los detalles con WebAuthenticationDetailsSource y lo guarda con SecurityContextHolder.getContext().setAuthentication(...)
 4) en todos los casos llama a filterChain.doFilter(request, response); si el token no es válido, no lances excepción ni escribas la respuesta.
 Usa jakarta.servlet, no javax.servlet. No uses UserDetailsService. Añade Javadoc en español en la clase y en todos los métodos. — generado por IA
*/
/**
 * Filtro de autenticación que intercepta las solicitudes HTTP y valida los tokens JWT.
 * Si el token es válido, establece la autenticación en el contexto de seguridad de Spring.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;

    /**
     * Constructor que inyecta el servicio JwtService.
     * @param jwtService El servicio que maneja la generación y validación de tokens JWT.
     */
	public JwtAuthenticationFilter(JwtService jwtService) {
		this.jwtService = jwtService;
	}

    /**
     * Autentica la petición a partir del token JWT de la cabecera {@code Authorization}.
	 * Si no hay cabecera {@code Bearer}, o el token no es válido, deja seguir la petición
	 * sin autenticar; será la autorización de Spring Security quien decida el acceso.
     * @param request petición HTTP entrante
     * @param response respuesta HTTP
     * @param filterChain cadena de filtros
     * @throws ServletException si falla la cadena de filtros
     * @throws IOException si falla la lectura de la petición o escritura de la respuesta
     */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.substring(7);
		if (jwtService.isTokenValid(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
			String username = jwtService.extractUsername(token);
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
			authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}

		filterChain.doFilter(request, response);
	}

	
}
