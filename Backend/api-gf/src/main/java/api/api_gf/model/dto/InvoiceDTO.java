package api.api_gf.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {
	private Integer invoiceId;
	private Integer userId;
	private Integer businessId;
	private Integer clientId;
	private String numInvoice;
	private LocalDate dateInvoice;
	private String addressWork;
	private BigDecimal subtotal;
	private BigDecimal iva;
	private BigDecimal total;
	private boolean paid;
	private List<InvoiceItemDTO> invoicesItems;
	
	private ClientDTO client;

}
