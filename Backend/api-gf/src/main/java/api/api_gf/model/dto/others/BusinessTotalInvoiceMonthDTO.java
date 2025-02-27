package api.api_gf.model.dto.others;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface BusinessTotalInvoiceMonthDTO {
	BigDecimal getTotalPrices();

	BigDecimal getTotalPricesNoPaid();

	BigDecimal getTotalPricesPaid();

	String getInvoiceMonth();

}
