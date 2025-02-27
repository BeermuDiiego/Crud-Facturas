package api.api_gf.model.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UserRegisterDTO {

	@NotNull(message = "El nombre es obligatorio.")
	String name;
	@NotNull(message = "El apellido es obligatorio.")
	String surname;
	@Email(message = "El correo electrónico debe ser válido")
	String email;
	@NotNull(message = "La contraseña es obligatoria.")
	String password;

}
