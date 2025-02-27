package api.api_gf.model.dto.others;

import java.math.BigDecimal;

public interface BusinessTotalInvoiceDTO {

	Integer getNumClients();

	Integer getNumInvoices();

	BigDecimal getTotalPrices();

	BigDecimal getTotalPricesNoPaid();

	BigDecimal getTotalPricesPaid();

}
