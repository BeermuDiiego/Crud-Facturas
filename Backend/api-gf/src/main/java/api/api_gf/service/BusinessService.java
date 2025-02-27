package api.api_gf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.api_gf.model.dto.BusinessDTO;
import api.api_gf.model.entity.Business;
import api.api_gf.model.entity.User;
import api.api_gf.model.mapper.BusinessMapper;
import api.api_gf.repository.BusinessRepository;
import api.api_gf.repository.InvoiceItemRepository;
import lombok.RequiredArgsConstructor;

/**
 * Servicio encargado de la gestion de los negocios.
 */
@Service
@RequiredArgsConstructor
public class BusinessService {

	@Autowired
	private BusinessRepository businessRepository;

	@Autowired
	private BusinessMapper businessMapper;

	/**
	 * Metodo para crear un nuevo negocio.
	 * 
	 * @param businessDTO Contiene los datos del negocio que se van a crear.
	 * @return true si el negocio fue creado correctamente, false si ya existe un
	 *         negocio con el mismo CIF.
	 */
	public boolean createBusiness(BusinessDTO businessDTO) {

		// Comprueba si ya existe un negocio con ese cif en la cuenta del usuario.
		if (businessRepository.existsByUserIdAndCifBusiness(getUserIdAuth(), businessDTO.getCifBusiness())) {
			return false;

		}
		// Asigna el id del usuario al negocio, y lo guarda.
		else {
			businessDTO.setUserId(getUserIdAuth());
			Business business = businessMapper.INSTANCE.toEntity(businessDTO);
			businessRepository.save(business);

			return true;
		}

	}

	/**
	 * Metodo para eliminar un negocio por su id.
	 * 
	 * @param businessId Id del negocio que se va a eliminar
	 * @return Devuelve la confirmacion de la eliminacion
	 */
	@Transactional
	public boolean deleteBusiness(Integer businessId) {
		businessRepository.deleteByBusinessId(businessId);
		return !businessRepository.existsByBusinessId(businessId);

	}

	/**
	 * Metodo que sirve para actualizar un negocio
	 * 
	 * @param businessDTO Contiene los datos del negocio.
	 * @return Devuelve la confirmacion.
	 */
	@Transactional
	public boolean updateBusiness(BusinessDTO businessDTO) {
		// Primero se guarda el los datos negocio antes de modificarlo.
		Business oldBusiness = businessRepository.findByUserIdAndBusinessId(getUserIdAuth(),
				businessDTO.getBusinessId());

		// Se comprueba si el antiguo negocio tiene el mismo cif que los nuevos datos
		// negocio
		boolean cifChange = !oldBusiness.getCifBusiness().equalsIgnoreCase(businessDTO.getCifBusiness());
		if (cifChange) {
			Integer num = businessRepository.existsByUserIdAndCifBusinessAndNotBusinessId(getUserIdAuth(),
					businessDTO.getCifBusiness(), businessDTO.getBusinessId());
			if (1 == num) {
				return false;
			}
		}

		// Si pasa las comprobaciones, se modifica los datos del negocio con los datos
		// obtenidos por parametros y se actualiza con save().
		oldBusiness.setCifBusiness(businessDTO.getCifBusiness());
		oldBusiness.setAccountNumber(businessDTO.getAccountNumber());
		oldBusiness.setAddress(businessDTO.getAddress());
		oldBusiness.setEmail(businessDTO.getEmail());
		oldBusiness.setNameBusiness(businessDTO.getNameBusiness());
		oldBusiness.setPhone(businessDTO.getNameBusiness());

		businessRepository.save(oldBusiness);

		return true;

	}

	/**
	 * Metodo para obtener la lista de negocios por id usuario.
	 * 
	 * @return Devuelve la lista de negocios.
	 */
	public List<BusinessDTO> getListBusiness() {

		List<Business> listBusiness = businessRepository.findByUserId((Integer) getUserIdAuth());

		return businessMapper.INSTANCE.ToDTOs(listBusiness);
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
