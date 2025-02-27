package api.api_gf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import api.api_gf.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;

/**
 * Clase de configuracion para la seguridad utilizando Spring Security.
 * Configura las politicas de autenticacion, autorizacion y gestion de sesiones.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;
	private final AuthenticationProvider authProvider;

	/**
	 * Metodo que configura la cadena de filtros de seguridad para la aplicacion.
	 * 
	 * @param HttpSecurity para configurar las politicas de seguridad.
	 * @return Filtros de seguridad que se ha configurado.
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				// Deshabilita la proteccion CSRF
				.csrf(csrf -> csrf.disable())
				// Configuracion de las reglas de autorizacion HTTP.
				.authorizeHttpRequests(authRequest -> authRequest
						// Permite el acceso sin autenticacion a las rutas que comienzan con "/auth".
						.requestMatchers("/auth/**").permitAll()
						// Requiere autenticacion para las rutas de clientes, negocios y facturas.
						.requestMatchers("/client/**", "/business/**", "/invoice/**").authenticated()
						// Tambien requiere autenticacion para cualquier otra ruta aparte.
						.anyRequest().authenticated())
				// Configura la gestion de sesiones para que sea sin estado.
				.sessionManagement(
						sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				// Establece el proveedor de autenticacion configurado.
				.authenticationProvider(authProvider)
				// Agrega el filtro JWT
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();

	}

}
