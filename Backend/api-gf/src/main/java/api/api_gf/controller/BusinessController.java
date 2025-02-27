package api.api_gf.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.api_gf.model.dto.BusinessDTO;
import api.api_gf.service.BusinessService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador con los edndpoints de creacion, eliminacion, actualizacion y
 * obtencion de negocios.
 */
@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessController {

	@Autowired
	private BusinessService businessService;

	/**
	 * Endpoint para crear un nuevo negocio.
	 * 
	 * @param businessDTO Tiene los datos del negocio que sera creado.
	 * @return - 202 (Accepted) Si el negocio fue creado. - 409 (Conflict) Si ya
	 *         existe un negocio con el mismo identificador. - 500 (Internal Server
	 *         Error) Si pasa un error inesperado.
	 */
	@PostMapping(value = "create")
	public ResponseEntity<String> createBusiness(@RequestBody @Valid BusinessDTO businessDTO) {

		try {

			if (businessService.createBusiness(businessDTO)) {
				return ResponseEntity.status(HttpStatus.ACCEPTED).body("Empresa Registrada");
			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya tienes un negocio con ese identificador");

			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Endpoint para eliminar un negocio. Se elimina el negocio por su id.
	 * 
	 * @param businessId Id del negocio que se va a eliminar.
	 * @return - 200 (OK) Si el negocio fue eliminado. - 409 (Conflict) Si no se
	 *         pudo eliminar el negocio. - 500 (Internal Server Error) Si pasa un
	 *         error inesperado.
	 */
	@DeleteMapping(value = "delete/{businessId}")
	public ResponseEntity<String> deleteBusiness(@PathVariable Integer businessId) {

		try {

			if (businessService.deleteBusiness(businessId)) {
				return ResponseEntity.status(HttpStatus.OK).body("Negocio eliminado");

			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Negocio no eliminada");

			}

		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	/**
	 * Endpoint para actualizar los datos de un negocio.
	 * 
	 * @param businessDTO Contiene los nuevos datos del negocio que se van a
	 *                    actualizar.
	 * @return - 202 (Accepted) Si el negocio fue actualizado. - 409 (Conflict) Si
	 *         ya existe un negocio con el mismo identificador (CIF/DNI). - 500
	 *         (Internal Server Error) Si pasa un error inesperado.
	 */
	@PutMapping(value = "update")
	public ResponseEntity<String> updateBusiness(@RequestBody BusinessDTO businessDTO) {
		try {

			if (businessService.updateBusiness(businessDTO)) {
				return ResponseEntity.status(HttpStatus.ACCEPTED).body("Negocio Editado");
			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya tienes un negocio con ese identificador");

			}

		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	/**
	 * Endpoint para obtener la lista de negocios.
	 * 
	 * @return - 200 (OK) Si la lista fue obtenida con exito. - 500 (Internal Server
	 *         Error) Si pasa un error inesperado.
	 */
	@GetMapping(value = "get-list")
	public ResponseEntity<List<BusinessDTO>> getBusinessUser() {
		try {

			return ResponseEntity.status(HttpStatus.OK).body(businessService.getListBusiness());

		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}

}
