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
import api.api_gf.model.dto.ClientDTO;
import api.api_gf.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador con endpoints para crear clientes y listar clientes asociados a
 * un negocio.
 */
@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

	@Autowired
	private ClientService clientService;

	/**
	 * Endpoint para crear un nuevo cliente.
	 * 
	 * @param invoiceDTO Contiene los datos del cliente que se va a crear.
	 * @return - 202 (Accepted) Si el cliente fue registrado. - 409 (Conflict) Si ya
	 *         existe un cliente con los mismos datos (Identificador). - 500
	 *         (Internal Server Error) En caso de un error inesperado.
	 */
	@PostMapping(value = "create")
	public ResponseEntity<String> createClient(@RequestBody @Valid ClientDTO clientDTO) {
		try {
			String result = clientService.createClient(clientDTO);

			if (result.equalsIgnoreCase("Cliente Registrado")) {
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(result);
			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(result);

			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Endpoint para eliminar un cliente.
	 * 
	 * @param invoiceId Id del cliente que se va eliminar.
	 * @return - 200 (OK) Si el cliente fue eliminado. - 409 (Conflict) Si no se
	 *         pudo eliminar el cliente. - 500 (Internal Server Error) En caso de un
	 *         error inesperado.
	 */
	@DeleteMapping(value = "delete/{clientId}")
	public ResponseEntity<String> deleteClient(@PathVariable Integer clientId) {

		try {

			if (clientService.deleteClient(clientId)) {
				return ResponseEntity.status(HttpStatus.OK).body("Cliente eliminado");

			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Cliente no eliminada");

			}

		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	/**
	 * Endpoint para actualizar un cliente.
	 * 
	 * @param invoiceDTO Contiene los datos del cliente que se va a actulizar.
	 * @return - 202 (Accepted) Si el cliente fue actualizado. - 409 (Conflict) Si
	 *         no se pudo actualizar el cliente porque ya existe un cliente con ese
	 *         identificador. - 500 (Internal Server Error) En caso de un error
	 *         inesperado.
	 */
	@PutMapping(value = "update")
	public ResponseEntity<String> updateClient(@RequestBody ClientDTO clientDTO) {
		try {

			if (clientService.updateClient(clientDTO)) {
				return ResponseEntity.status(HttpStatus.ACCEPTED).body("Cliente Editado");
			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya tienes un cliente con ese identificador");

			}

		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	/**
	 * Endpoint para obtener la lista de clientes de un negocio.
	 * 
	 * @param businessId El id del negocio
	 * @return Devuelve la lista de clientes por negocio
	 */
	@GetMapping(value = "list-clients/{businessId}")
	public ResponseEntity<List<ClientDTO>> getListClients(@PathVariable Integer businessId) {

		try {
			return ResponseEntity.status(HttpStatus.OK).body(clientService.getListClients(businessId));

		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

}
