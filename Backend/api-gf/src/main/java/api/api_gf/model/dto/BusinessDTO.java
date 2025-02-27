package api.api_gf.model.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDTO {
	private Integer businessId;
	private Integer userId;
	@NotNull(message = "El nombre de la empresa es obligatorio.")
	private String nameBusiness;
	@NotNull(message = "El CIF o DNI/NIF es obligatorio.")
	@Pattern(regexp = "^[A-Za-z]\\d{7}[0-9A-Za-z]$|^[0-9]{8}[A-Za-z]$", message = "El identificador debe ser un CIF (letra seguida de 7 dígitos y un dígito de control) o un DNI/NIF (8 dígitos y una letra).")
	private String cifBusiness;
	@NotNull(message = "La dirección de correo es obligatoria.")
	@Email(message = "El formato del email no es válido.")
	private String email;
	@NotNull(message = "La dirección de la empresa es obligatoria.")
	private String address;
	private String phone;
	@NotNull(message = "La cuenta de la empresa es obligatoria.")
	private String accountNumber;
	private List<ClientDTO> clients;
}
