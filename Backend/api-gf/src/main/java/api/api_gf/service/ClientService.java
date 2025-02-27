package api.api_gf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.api_gf.model.dto.ClientDTO;
import api.api_gf.model.entity.Client;
import api.api_gf.model.entity.User;
import api.api_gf.model.mapper.ClientMapper;
import api.api_gf.repository.BusinessRepository;
import api.api_gf.repository.ClientRepository;
import lombok.RequiredArgsConstructor;

/**
 * Servicio para la gestion de los clientes.
 */
@Service
@RequiredArgsConstructor
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private BusinessRepository businessRepository;
	@Autowired
	private ClientMapper clientMapper;

	/**
	 * Metodo que crea un nuevo cliente.
	 * 
	 * @param clientDTO Contiene los datos del cliente que se van a crear.
	 * @return Un mensaje indicando el resultado de la operacion.
	 */
	public String createClient(ClientDTO clientDTO) {
		// Primero se obtiene el id del usuario por el token.
		Integer userId = getUserIdAuth();

		// Comprueba si existe el negocio
		if (businessRepository.existsByUserIdAndBusinessId(userId, clientDTO.getBusinessId())) {
			// Luego comprueba si ya existe un cliente con el identificador en el negocio
			// del usuario.
			if (clientRepository.existsByBusinessBusinessIdAndCifClient(clientDTO.getBusinessId(),
					clientDTO.getCifClient())) {
				return "Ya tienes un cliente con ese identificador";
			}
			// Antes de guardar, asigna el id del usuario al cliente, luego mapea, y por
			// ultimo guarda el nuevo cliente.
			else {
				clientDTO.setUserId(userId);
				Client client = clientMapper.INSTANCE.toEntity(clientDTO);
				clientRepository.save(client);
				return "Cliente Registrado";
			}

		} else {
			return "No existe la empresa";
		}

	}

	/**
	 * Metodo que sirve para obtener la lista de clientes por negocio.
	 * 
	 * Se comrpueba por id del usuario y del negocio
	 * 
	 * @param businessId Id del negocio
	 * @return Devuelve la lista de clientes del negocio
	 */
	public List<ClientDTO> getListClients(Integer businessId) {
		List<Client> listClients = clientRepository.findByUserIdAndBusinessBusinessId(getUserIdAuth(), businessId);
		return clientMapper.toDTOs(listClients);
	}

	/**
	 * Metodo para eliminar un cliente por su id.
	 * 
	 * @param businessId Id del cliente que se va a eliminar
	 * @return Devuelve la confirmacion de la eliminacion
	 */
	@Transactional
	public boolean deleteClient(Integer clientId) {
		clientRepository.deleteByClientId(clientId);
		return !clientRepository.existsByClientId(clientId);
	}

	/**
	 * Metodo que sirve para actualizar un cliente
	 * 
	 * @param businessDTO Contiene los datos del negocio.
	 * @return Devuelve la confirmacion.
	 */
	@Transactional
	public boolean updateClient(ClientDTO clientDTO) {
		// Primero se guarda en los datos del cliente antes de modificarlo.
		Client oldClient = clientRepository.findByUserIdAndBusinessBusinessIdAndClientId(getUserIdAuth(),
				clientDTO.getBusinessId(), clientDTO.getClientId());

		// Se comprueba si el antiguo cliente tiene el mismo CIF que los nuevos datos
		boolean cifChange = !oldClient.getCifClient().equalsIgnoreCase(clientDTO.getCifClient());
		if (cifChange) {
			// Verificar si ya existe otro cliente con el mismo cif dentro de la empresa
			Integer num = clientRepository.existsByBusinessBusinessIdAndCifClientAndNotClientId(
					clientDTO.getBusinessId(), clientDTO.getCifClient(), clientDTO.getClientId());
			if (num == 1) {
				return false;
			}
		}

		// Si pasa las comprobaciones, se actualizan los datos del cliente
		oldClient.setCifClient(clientDTO.getCifClient());
		oldClient.setNameClient(clientDTO.getNameClient());
		oldClient.setEmail(clientDTO.getEmail());
		oldClient.setAddress(clientDTO.getAddress());

		clientRepository.save(oldClient);

		return true;

	}

	/**
	 * Metodo que sirve para obtener el id del usuario por el token.
	 * 
	 * @return Devuelve el id del usuario.
	 */
	private Integer getUserIdAuth() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User userDetails = (User) auth.getPrincipal();

		return userDetails.getId();
	}

}
