package api.api_gf.model.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceItemDTO {
	private Integer itemId;
	private Integer invoiceId;
	private String serviceName;
	private Integer quantity;
	private BigDecimal price;
	private BigDecimal total;

}
