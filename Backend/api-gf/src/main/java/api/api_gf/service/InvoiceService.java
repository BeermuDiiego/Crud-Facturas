package api.api_gf.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.api_gf.model.dto.InvoiceDTO;
import api.api_gf.model.dto.InvoiceItemDTO;
import api.api_gf.model.dto.others.BusinessTotalInvoiceDTO;
import api.api_gf.model.dto.others.BusinessTotalInvoiceDayDTO;
import api.api_gf.model.dto.others.BusinessTotalInvoiceMonthDTO;
import api.api_gf.model.dto.others.BusinessTotalInvoiceYearDTO;
import api.api_gf.model.dto.others.ClientTotalInvoiceDTO;
import api.api_gf.model.dto.others.InvoiceTotalDTO;
import api.api_gf.model.entity.Invoice;
import api.api_gf.model.entity.InvoiceItem;
import api.api_gf.model.entity.User;
import api.api_gf.model.mapper.InvoiceItemMapper;
import api.api_gf.model.mapper.InvoiceMapper;
import api.api_gf.repository.ClientRepository;
import api.api_gf.repository.InvoiceItemRepository;
import api.api_gf.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;

/**
 * Servicio para manejar las operaciones relacionadas con las facturas.
 */
@Service
@RequiredArgsConstructor
public class InvoiceService {

	@Autowired
	private InvoiceRepository invoiceRepository;
	@Autowired
	private InvoiceItemRepository invoiceItemRepository;
	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private InvoiceMapper invoiceMapper;
	@Autowired
	private InvoiceItemMapper invoiceItemMapper;

	/**
	 * Metodo para crear una nueva factura.
	 * 
	 * @param invoiceDTO Datos de la factura que se van a crear.
	 * @return Confirmacion de la creacion de la factura.
	 */
	public boolean createInvoice(InvoiceDTO invoiceDTO) {

		// Se obtiene el id del usuario
		Integer userId = getUserIdAuth();

		// Se comprueba si existe el usuario, el negocio, y el cliente.
		if (clientRepository.existsByUserIdAndBusinessBusinessIdAndClientId(userId, invoiceDTO.getBusinessId(),
				invoiceDTO.getClientId())) {
			// Tambien se comprueba que no se repita el numero de factura.
			if (invoiceRepository.existsByClientClientIdAndNumInvoice(invoiceDTO.getClientId(),
					invoiceDTO.getNumInvoice())) {

				return false;

			} else {
				// Se asigna el id del usuario a la factura
				invoiceDTO.setUserId(userId);
				// Se extrae los items de la factura para ser mapeados
				List<InvoiceItemDTO> invoiceItemDTOs = invoiceDTO.getInvoicesItems();

				invoiceDTO.setInvoicesItems(null);
				// Se mapea la nueva factura, y se guarda en la bbdd, pero sin los items para
				// evitar fallos.
				Invoice invoice = invoiceMapper.toEntity(invoiceDTO);
				invoiceRepository.save(invoice);

				// Se recorrera la lista de items para asignar la id de la factura.
				List<InvoiceItem> invoiceItems = new ArrayList<>();
				InvoiceItem invoiceItem = null;

				for (InvoiceItemDTO invoiceItemDTO : invoiceItemDTOs) {
					invoiceItemDTO.setInvoiceId(invoice.getInvoiceId());
					invoiceItem = invoiceItemMapper.toEntity(invoiceItemDTO);
					invoiceItems.add(invoiceItem);
				}

				invoice.setInvoicesItems(invoiceItems);

				// Se actualiza la factura con los items y se guarda los items.
				invoiceRepository.save(invoice);
				invoiceItemRepository.saveAll(invoiceItems);

				return true;

			}

		} else {
			return false;
		}

	}

	/**
	 * Metodo para actualizar una factura.
	 * 
	 * @param invoiceDTO Datos de la factura que se va actualizar.
	 * @return true si la actualización es exitosa, false en caso de error.
	 */
	@Transactional
	public boolean updateInvoice(InvoiceDTO invoiceDTO) {
		try {
			// Primero se borra la factura, y luego se vuelve a crear con los datos
			// actualizados.
			// Esto porque daba fallo al intentar actualizar con la misma id.
			deleteInvoice(invoiceDTO.getInvoiceId());
			invoiceDTO.setInvoiceId(null);
			createInvoice(invoiceDTO);
			return true;

		} catch (Exception e) {
			System.out.println("Error Actualizacion: " + e);
			return false;
		}
	}

	/**
	 * Metodo para obtener la lista de facturas por negocio
	 * 
	 * @param businessId Id del negocio
	 * @return Devuelve la lista de facturas
	 */
	public List<InvoiceDTO> getListInvoices(Integer businessId) {
		// Se busca por id del usuario y por id del negocio
		List<Invoice> listInvoices = invoiceRepository.findByUserIdAndBusinessBusinessId(getUserIdAuth(), businessId);

		return invoiceMapper.toDTOs(listInvoices);
	}

	/**
	 * Metodo para eliminar una factura.
	 * 
	 * Borra directamente con el id de la factura.
	 * 
	 * @param invoiceId Id de la factura.
	 * @return Devuelve la confirmacion del borrado.
	 */
	@Transactional
	public boolean deleteInvoice(Integer invoiceId) {
		invoiceRepository.deleteByInvoiceId(invoiceId);
		return !invoiceRepository.existsByInvoiceId(invoiceId);
	}

	/**
	 * Meotodo para obtener la lista de facturas por su total
	 * 
	 * @param businessId Id del negocio
	 * @return Devuelve la lista de facturas totales
	 */
	public List<InvoiceTotalDTO> getListInvoicesTotal(Integer businessId) {
		return invoiceRepository.findTotalInvoices(getUserIdAuth(), businessId);
	}

	/**
	 * Metodo para obtener el total de facturas por negocio en una lista.
	 * 
	 * @param businessId Id del negocio
	 * @return Devuelve el total de facturas.
	 */
	public List<BusinessTotalInvoiceDTO> getTotalPricesByBusiness(Integer businessId) {
		return invoiceRepository.findTotalPricesByBusiness(businessId, getUserIdAuth());
	}

	/**
	 * Metodo para obtener el total de facturas por negocio en una lista pero por
	 * dia.
	 * 
	 * @param businessId Id del negocio
	 * @return Devuelve el total de facturas por dia.
	 */
	public List<BusinessTotalInvoiceDayDTO> getTotalPricesByBusinessDay(Integer businessId) {
		return invoiceRepository.findTotalPricesByBusinessDay(businessId, getUserIdAuth());
	}

	/**
	 * Metodo para obtener el total de facturas por negocio en una lista pero por
	 * mes.
	 * 
	 * @param businessId Id del negocio
	 * @return Devuelve el total de facturas por mes.
	 */
	public List<BusinessTotalInvoiceMonthDTO> getTotalPricesByBusinessMonth(Integer businessId) {
		return invoiceRepository.findTotalPricesByBusinessMonth(businessId, getUserIdAuth());
	}

	/**
	 * Metodo para obtener el total de facturas por negocio en una lista pero por
	 * anyo.
	 * 
	 * @param businessId Id del negocio
	 * @return Devuelve el total de facturas por anyo.
	 */
	public List<BusinessTotalInvoiceYearDTO> getTotalPricesByBusinessYear(Integer businessId) {
		return invoiceRepository.findTotalPricesByBusinessYear(businessId, getUserIdAuth());
	}

	/**
	 * Metodo para obtener la lista de facturas totales por cliente.
	 * 
	 * @param businessId Id del negocio
	 * @return Devuelve una lista de clientes con sus totales.
	 */
	public List<ClientTotalInvoiceDTO> getTotalPricesClients(Integer businessId) {
		return invoiceRepository.findTotalPricesClients(businessId, getUserIdAuth());
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