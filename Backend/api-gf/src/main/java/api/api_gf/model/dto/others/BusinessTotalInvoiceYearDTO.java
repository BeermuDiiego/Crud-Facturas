package api.api_gf.model.dto.others;

import java.math.BigDecimal;

public interface BusinessTotalInvoiceYearDTO {
	BigDecimal getTotalPrices();

	BigDecimal getTotalPricesNoPaid();

	BigDecimal getTotalPricesPaid();

	Integer getInvoiceYear();

}
