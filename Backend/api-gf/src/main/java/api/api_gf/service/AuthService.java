package api.api_gf.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import api.api_gf.jwt.JwtService;
import api.api_gf.model.dto.AuthResponse;
import api.api_gf.model.dto.UserLoginDTO;
import api.api_gf.model.dto.UserRegisterDTO;
import api.api_gf.model.entity.Role;
import api.api_gf.model.entity.User;
import api.api_gf.model.mapper.UserMapper;
import api.api_gf.repository.UserRepository;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de la autenticacion de usuarios.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final UserMapper userMapper;

	/**
	 * Metodo que hace el login del usuario.
	 * 
	 * @param request Contiene las credenciales del usuario.
	 * @return Token de autenticacion.
	 */
	public AuthResponse login(UserLoginDTO request) {
		// Autentica al usuario utilizando el email y la contrasenya.
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		// Obtiene los datos del usuario de la bbdd, y crea el token para el usuario.
		UserDetails user = userRepository.findByEmail(request.getEmail()).orElseThrow();
		String token = jwtService.getToken(user);
		// Devuelve el token creado para el usuario.
		return AuthResponse.builder().token(token).build();

	}

	/**
	 * Metodo para registrar un nuevo usuario.
	 * 
	 * @param request Contiene los datos del nuevo usuario.
	 * @return true si el usuario fue registrado, false si ya existe un usuario con
	 *         el mismo email.
	 */
	public boolean register(UserRegisterDTO request) {
		// Comprueba que no exista un usuario con el email dado.
		if (userRepository.existsByEmail(request.getEmail())) {
			return false;
		} else {
			// Mapea, asigna la contrasenya codificado, y lo guarda en la bbdd
			User user = userMapper.INSTACE.toEntity(request);
			user.setRole(Role.USER);
			user.setPassword(passwordEncoder.encode(request.getPassword()));

			userRepository.save(user);

			return true;
		}

	}

}
