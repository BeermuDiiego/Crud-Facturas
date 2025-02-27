package api.api_gf.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Clase para configurar CORS.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

	/**
	 * Metodo para la configuracion de CORS.
	 * 
	 * @return La configuracion CORS que sera utilizada por Spring.
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		// Origenes permitidos
		configuration.setAllowedOrigins(List.of("http://localhost:5173"));
		// Metodos permitidos
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		// Cabeceras permitidas
		configuration.setAllowedHeaders(List.of("Content-Type", "Authorization"));
		// Cabeceras expuestas
		configuration.setExposedHeaders(List.of("Authorization"));
		// Permite las credenciales
		configuration.setAllowCredentials(true);
		// Configura el tiempo maximo
		configuration.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
