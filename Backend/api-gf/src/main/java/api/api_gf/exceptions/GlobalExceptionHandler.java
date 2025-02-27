package api.api_gf.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Clase manejadora global de excepciones. Utiliza `@ControllerAdvice` para
 * interceptar excepciones en los controladores y dar mejores respuestas.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Maneja excepciones de tipo MethodArgumentNotValidException que ocurren cuando
	 * las validaciones de datos de entrada fallan (@Valid).
	 * 
	 * @param ex La excepción que contiene información sobre los errores de
	 *           validación.
	 * @return Un mapa con los campos inválidos como clave y los mensajes de error
	 *         como valor, envuelto en una respuesta HTTP con estado 400 (Bad
	 *         Request).
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		// Almacena los errores
		Map<String, String> errors = new HashMap<>();
		// Recorre todos los errores y obtiene el nombre del campo y el mensaje del
		// error.
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((org.springframework.validation.FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
}
