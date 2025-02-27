package api.api_gf.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import api.api_gf.model.dto.AuthResponse;
import api.api_gf.model.dto.UserLoginDTO;
import api.api_gf.model.dto.UserRegisterDTO;
import api.api_gf.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador con los endpoints de autenticacion de usuarios y registro de
 * usuarios.
 * 
 * Cualquiera puede acceder.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", methods = { RequestMethod.GET, RequestMethod.POST,
		RequestMethod.OPTIONS })
public class AuthController {

	private final AuthService authService;

	/**
	 * Endpoint POST para registrar un nuevo usuario.
	 * 
	 * @param request Contiene los datos del usuario a registrar.
	 * @return - 202 (Accepted) Si el usuario fue creado. - 409 (Conflict) Si el
	 *         usuario no pudo ser creado. - 500 (Internal Server Error) Si hay un
	 *         error inesperado.
	 */
	@PostMapping(value = "register")
	public ResponseEntity<String> register(@Valid @RequestBody UserRegisterDTO request) {

		try {
			if (authService.register(request)) {
				return ResponseEntity.status(HttpStatus.ACCEPTED).body("Usuario Creado");

			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Usuario No Creado");
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	/**
	 * Endpoint POST para autenticar a un usuario,devolviendo un token.
	 * 
	 * @param request Contiene las credenciales del usuario que se logea.
	 * @return Si el logeo es bueno, devuelve el token,si ocurre un en el logeo
	 *         fallo, devuelve un null.
	 */
	@PostMapping(value = "login")
	public ResponseEntity<AuthResponse> login(@RequestBody UserLoginDTO request) {
		try {
			return ResponseEntity.ok(authService.login(request));
		} catch (BadCredentialsException e) {
			return ResponseEntity.ok(null);
		}

	}

}
