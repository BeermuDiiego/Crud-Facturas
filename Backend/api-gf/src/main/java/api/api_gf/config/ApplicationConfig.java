package api.api_gf.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import api.api_gf.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * Clase de configuracion para la configuracion de seguridad de la API,
 * incluyendo la gestion de autenticacion y los detalles del usuario.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

	private final UserRepository userRepository;

	/**
	 * Metodo que da un bean para el AuthenticationManager.
	 * 
	 * @param config Configuracion de autenticacion de Spring Security.
	 * @return El AuthenticationManager ya configurado.
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	/**
	 * Metodo que da un bean para el AuthenticationProvider, que utiliza un
	 * DaoAuthenticationProvider con detalles de usuario y un codificador de
	 * contrasenyas.
	 * 
	 * @return El AuthenticationProvider ya configurado.
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	/**
	 * Metodo que proporciona un bean para el codificador de contrasenyas. Se usa
	 * BCryptPasswordEncoder para encriptar las contrasenyas.
	 * 
	 * @return El PasswordEncoder ya configurado.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Metodo que da un bean para el UserDetailsService. Utiliza el repositorio de
	 * usuarios para obtener los detalles del usuario basados en el email
	 * 
	 * @return El UserDetailsService configurado.
	 */
	@Bean
	public UserDetailsService userDetailService() {
		return username -> userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not fournd"));
	}

}
