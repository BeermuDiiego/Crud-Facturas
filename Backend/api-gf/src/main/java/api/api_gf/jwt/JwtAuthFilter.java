package api.api_gf.jwt;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Filtro de autenticación JWT. Este filtro intercepta cada solicitud HTTP para
 * verificar si existe un token JWT válido en la cabecera de autorización. Si el
 * token es valido, se autentica al usuario.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	/**
	 * Método principal del filtro. Se ejecuta en cada solicitud para validar el
	 * token y autenticar al usuario.
	 *
	 * @param request     Solicitud HTTP.
	 * @param response    Respuesta HTTP.
	 * @param filterChain La cadena de filtros para continuar con el procesamiento.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Extrae el token desde la cabecera de autorización.
		final String token = getTokenFromRequest(request);
		final String username;

		// Si no se encuentra un token, continúa con el siguiente filtro.
		if (token == null) {
			filterChain.doFilter(request, response);
			return;
		}

		// Obtiene el nombre de usuario desde el token.
		username = jwtService.getUsernameFromToken(token);

		// Verifica si el usuario no está autenticado.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// Obtiene los datos del usuario desde el servicio de usuarios.
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			// Valida el token con los datos del usuario.
			if (jwtService.isTokenValid(token, userDetails)) {
				// Crea un objeto de autenticación con los datos del usuario.
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				// Establece detalles adicionales para la autenticación.
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// Establece el objeto de autenticación en el contexto de seguridad.
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}

		}
		// Continúa con el procesamiento de la solicitud.
		filterChain.doFilter(request, response);
	}

	/**
	 * Extrae el token desde la cabecera de autorización de la solicitud HTTP.
	 *
	 * @param request Solicitud HTTP.
	 * @return El token si está presente y tiene el prefijo "Bearer ", de lo
	 *         contrario, null.
	 */
	private String getTokenFromRequest(HttpServletRequest request) {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		// Verifica que la cabecera tenga texto y comience con "Bearer ".
		if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {

			// Retorna el token eliminando el prefijo "Bearer ".
			return authHeader.substring(7);
		}
		return null;

	}

}
