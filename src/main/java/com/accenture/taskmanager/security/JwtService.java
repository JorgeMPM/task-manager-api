package com.accenture.taskmanager.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Servicio que genera, lee y valida tokens JWT firmados con HS256.
 * Centraliza el uso de la librería jjwt y de la clave secreta de la aplicación.
 */
@Service
public class JwtService {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expiration;

    /**
     * Obtiene la clave secreta utilizada para firmar los tokens JWT.
     * @return La clave secreta como un objeto SecretKey.
     */
	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Genera un token JWT para el usuario especificado.
	 * @param username El nombre de usuario para el que se generará el token.
	 * @return El token JWT generado.
	 */
	public String generateToken(String username) {
        Date now = new Date();
		return Jwts.builder()
				.subject(username)
				.issuedAt(now)
				.expiration(new Date(now.getTime() + expiration))
				.signWith(getSigningKey())
				.compact();
	}

    /**
    * Extrae el nombre de usuario del token JWT proporcionado.
    * @param token El token JWT del cual se extraerá el nombre de usuario.
    * @return El nombre de usuario extraído del token.
    * @throws io.jsonwebtoken.JwtException si el token está mal formado, tiene una firma inválida o ha expirado
    */
    public String extractUsername(String token) {
		return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
    * Verifica si el token JWT proporcionado es válido.
    * @param token El token JWT a verificar.
    * @return true si el token es válido y no ha expirado; false en caso contrario.
    */
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    
}