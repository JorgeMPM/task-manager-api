package com.accenture.taskmanager.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/*
 Crea la clase JwtService anotada con @Service, con Javadoc de clase en español. Inyecta con @Value las propiedades jwt.secret (String) y jwt.expiration (long, en milisegundos).
 Añade un método privado getSigningKey() que devuelva un SecretKey creado con Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), sin decodificar en Base64.
 Añade el método generateToken(String username) que construya el JWT con Jwts.builder() incluyendo subject (username), issuedAt (fecha actual) y expiration (fecha actual + jwt.expiration), lo firme con signWith(getSigningKey()) y devuelva compact().
 Usa la API de jjwt 0.13 (subject(), issuedAt(), expiration()), sin métodos deprecated como setSubject() ni la clase SignatureAlgorithm. 
 Añade Javadoc en español a nivel de clase y en todos los métodos. — generado por IA
*/
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

    /*
	Añade el método extractUsername(String token) que verifique la firma con Jwts.parser().verifyWith(getSigningKey()).build(), obtenga los claims con parseSignedClaims(token).getPayload() y devuelva getSubject().
	Usa la API de jjwt 0.13, sin parserBuilder(), setSigningKey() ni parseClaimsJws(). Añade Javadoc en español. — generado por IA
	*/
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

    /*
	Añade el método isTokenValid(String token) que devuelva true si el token tiene una firma válida y no ha expirado, verificándolo con Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token),
	y que devuelva false si se lanza JwtException o IllegalArgumentException (token mal formado, firma inválida, expirado, nulo o vacío), sin propagar la excepción. Añade Javadoc en español. — generado por IA
	*/
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