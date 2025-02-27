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

import api.api_gf.model.dto.InvoiceDTO;
import api.api_gf.model.dto.others.BusinessTotalInvoiceDTO;
import api.api_gf.model.dto.others.BusinessTotalInvoiceDayDTO;
import api.api_gf.model.dto.others.BusinessTotalInvoiceMonthDTO;
import api.api_gf.model.dto.others.BusinessTotalInvoiceYearDTO;
import api.api_gf.model.dto.others.ClientTotalInvoiceDTO;
import api.api_gf.model.dto.others.InvoiceTotalDTO;
import api.api_gf.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controlador con endpoints para CRUD basico de facturas y para obtener
 * estadisticas sobre las facturas.
 */
@RestController
@RequestMapping("/invoice")
@RequiredArgsConstructor
public class InvoiceController {

	@Autowired
	private InvoiceService invoiceService;

	/**
	 * Endpoint para crear una nueva factura.
	 * 
	 * @param invoiceDTO Contiene los datos de la factura que se va a crear.
	 * @return - 202 (Accepted) Si la factura fue registrada. - 409 (Conflict) Si ya
	 *         existe una factura con los mismos datos (Numero de factura). - 500
	 *         (Internal Server Error) En caso de un error inesperado.
	 */
	@PostMapping(value = "create")
	public ResponseEntity<String> createInvoice(@RequestBody @Valid InvoiceDTO invoiceDTO) {

		try {
			if (invoiceService.createInvoice(invoiceDTO)) {

				return ResponseEntity.status(HttpStatus.ACCEPTED).body("Factura Registrada");

			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Factura No Registrada");

			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Endpoint para eliminar una factura.
	 * 
	 * @param invoiceId Id de la factura que se va eliminar.
	 * @return - 200 (OK) Si la factura fue eliminada. - 409 (Conflict) Si no se
	 *         pudo eliminar la factura. - 500 (Internal Server Error) En caso de un
	 *         error inesperado.
	 */
	@DeleteMapping(value = "delete/{invoiceId}")
	public ResponseEntity<String> deleteInvoice(@PathVariable Integer invoiceId) {

		try {

			if (invoiceService.deleteInvoice(invoiceId)) {
				return ResponseEntity.status(HttpStatus.OK).body("Factura eliminada");

			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Factura no eliminada");

			}

		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	/**
	 * Endpoint para actualizar una factura.
	 * 
	 * @param invoiceDTO Contiene los datos de la factura que se va a actulizar.
	 * @return - 202 (Accepted) Si la factura fue actualizada. - 409 (Conflict) Si
	 *         no se pudo actualizar la factura. - 500 (Internal Server Error) En
	 *         caso de un error inesperado.
	 */
	@PutMapping(value = "update")
	public ResponseEntity<String> updateInvoice(@RequestBody @Valid InvoiceDTO invoiceDTO) {
		try {
			if (invoiceService.updateInvoice(invoiceDTO)) {

				return ResponseEntity.status(HttpStatus.ACCEPTED).body("Factura Actualizada");

			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("Factura No Actualizada");

			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	/**
	 * Endpoint para obtener la lista de facturas asociadas a un negocio.
	 * 
	 * @param businessId Id del negocio.
	 * @return Lista de facturas del negocio, o en caso de error, devuelve un 500
	 *         (Internal Server Error).
	 */
	@GetMapping(value = "list-invoices/{businessId}")
	public ResponseEntity<List<InvoiceDTO>> getListInvoices(@PathVariable Integer businessId) {

		try {
			return ResponseEntity.status(HttpStatus.OK).body(invoiceService.getListInvoices(businessId));

		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

	}

	/**
	 * Endpoint para obtener el total de facturas asociadas a un negocio.
	 * 
	 * @param businessId Id del negocio.
	 * @return Lista de totales por factura del negocio, o en caso de error,
	 *         devuelve un 500 (Internal Server Error).
	 */
	@GetMapping(value = "list-invoices-total/{businessId}")
	public ResponseEntity<List<InvoiceTotalDTO>> getListInvoicesTotal(@PathVariable Integer businessId) {

		try {
			return ResponseEntity.status(HttpStatus.OK).body(invoiceService.getListInvoicesTotal(businessId));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Endpoint para obtener el total de ingresos por negocio.
	 * 
	 * @param businessId Id del negocio.
	 * @return Lista de totales de ingresos por negocio, o en caso de error,
	 *         devuelve un 500 (Internal Server Error).
	 */
	@GetMapping(value = "list-total-prices-business/{businessId}")
	public ResponseEntity<List<BusinessTotalInvoiceDTO>> getTotalPricesByBusiness(@PathVariable Integer businessId) {

		try {
			return ResponseEntity.status(HttpStatus.OK).body(invoiceService.getTotalPricesByBusiness(businessId));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Endpoint para obtener el total de ingresos por negocio, pero por dia.
	 * 
	 * @param businessId Id del negocio.
	 * @return Lista de totales de ingresos por negocio por dia, o en caso de error,
	 *         devuelve un 500 (Internal Server Error).
	 */
	@GetMapping(value = "list-total-prices-day-business/{businessId}")
	public ResponseEntity<List<BusinessTotalInvoiceDayDTO>> getTotalPricesByBusinessDay(
			@PathVariable Integer businessId) {

		try {
			return ResponseEntity.status(HttpStatus.OK).body(invoiceService.getTotalPricesByBusinessDay(businessId));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Endpoint para obtener el total de ingresos por negocio, pero por mes.
	 * 
	 * @param businessId Id del negocio.
	 * @return Lista de totales de ingresos por negocio por mes, o en caso de error,
	 *         devuelve un 500 (Internal Server Error).
	 */
	@GetMapping(value = "list-total-prices-month-business/{businessId}")
	public ResponseEntity<List<BusinessTotalInvoiceMonthDTO>> getTotalPricesByBusinessMonth(
			@PathVariable Integer businessId) {

		try {
			return ResponseEntity.status(HttpStatus.OK).body(invoiceService.getTotalPricesByBusinessMonth(businessId));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Endpoint para obtener el total de ingresos por negocio, pero por anyo.
	 * 
	 * @param businessId Id del negocio.
	 * @return Lista de totales de ingresos por negocio por anyo, o en caso de
	 *         error, devuelve un 500 (Internal Server Error).
	 */
	@GetMapping(value = "list-total-prices-year-business/{businessId}")
	public ResponseEntity<List<BusinessTotalInvoiceYearDTO>> getTotalPricesByBusinessYear(
			@PathVariable Integer businessId) {

		try {
			return ResponseEntity.status(HttpStatus.OK).body(invoiceService.getTotalPricesByBusinessYear(businessId));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Endpoint para obtener el total de ingresos por cliente.
	 * 
	 * @param businessId Id del negocio.
	 * @return Lista de totales de ingresos por cliente, o en caso de error,
	 *         devuelve un 500 (Internal Server Error).
	 */
	@GetMapping(value = "list-total-prices-clients/{businessId}")
	public ResponseEntity<List<ClientTotalInvoiceDTO>> getTotalPricesClients(@PathVariable Integer businessId) {

		try {
			return ResponseEntity.status(HttpStatus.OK).body(invoiceService.getTotalPricesClients(businessId));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
