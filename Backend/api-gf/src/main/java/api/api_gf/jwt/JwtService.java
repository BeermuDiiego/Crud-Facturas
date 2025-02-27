package api.api_gf.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Servicio para gestionar la creacion, validación y manipulación de tokens
 */
@Service
public class JwtService {
	// Clave secreta utilizada para firmar y verificar los tokens JWT.
	private static final String SECRET_KEY = "586E3272357538782F413F4428472B4B6250655368566B597033733676397924";

	/**
	 * Genera un token JWT para el usuario.
	 * 
	 * @param user Detalles del usuario.
	 * @return Token JWT creado.
	 */
	public String getToken(UserDetails user) {
		return getToken(new HashMap<>(), user);
	}

	/**
	 * Genera un token JWT con reclamos personalizados y detalles del usuario.
	 * 
	 * @param extraClaims Reclamos adicionales para incluir en el token.
	 * @param user        Detalles del usuario.
	 * @return Token JWT creado.
	 */
	private String getToken(Map<String, Object> extraClaims, UserDetails user) {
		return Jwts.builder()
				// Establece reclamos adicionales.
				.setClaims(extraClaims)
				// Establece el usuario como sujeto del token.
				.setSubject(user.getUsername())
				// Fecha de creacion.
				.setIssuedAt(new Date(System.currentTimeMillis()))
				// Tiempo de expiracion (24 horas)
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
				// Se firma el token con la clave secreta y algoritmo HS256.
				.signWith(getKey(), SignatureAlgorithm.HS256).compact();
	}

	/**
	 * Decodifica la clave secreta y la convierte en un objeto de tipo Key para la
	 * firma del token.
	 * 
	 * @return Clave secreta como objeto Key.
	 */
	private Key getKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * Obtiene el nombre de usuario del token JWT.
	 * 
	 * @param token El token JWT.
	 * @return Nombre de usuario extraído del token (Id).
	 */
	public String getUsernameFromToken(String token) {
		return getClaim(token, Claims::getSubject);
	}

	/**
	 * Verificacion de un token para saber si es válido para un usuario.
	 * 
	 * @param token       El token JWT.
	 * @param userDetails Detalles del usuario.
	 * @return true Si el token es válido, si es false, el token no es valido.
	 */
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	/**
	 * Obtiene todos los reclamos que tiene un token JWT.
	 * 
	 * @param token El token JWT.
	 * @return Los reclamos como un objeto Claims.
	 */
	private Claims getAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
	}

	/**
	 * Obtiene un reclamo específico del token JWT
	 * 
	 * @param token          El token JWT.
	 * @param claimsResolver La función para resolver el reclamo.
	 * @return Reclamo resuelto.
	 */
	public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaims(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Obtiene la fecha de expiración del token JWT.
	 * 
	 * @param token El token JWT.
	 * @return La fecha de expiración.
	 */
	private Date getExpiration(String token) {
		return getClaim(token, Claims::getExpiration);
	}

	/**
	 * Verifica si el token JWT ha expirado.
	 * 
	 * @param token El token JWT.
	 * @return true Si el token ha expirado, de lo contrario, false si aun sigue
	 *         activo.
	 */
	private boolean isTokenExpired(String token) {
		return getExpiration(token).before(new Date());
	}

}
