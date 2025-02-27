package api.api_gf.model.dto.others;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface BusinessTotalInvoiceDayDTO {
	BigDecimal getTotalPrices();

	BigDecimal getTotalPricesNoPaid();

	BigDecimal getTotalPricesPaid();

	LocalDate getInvoiceDay();

}
