package api.api_gf.model.dto.others;

import java.math.BigDecimal;

public interface ClientTotalInvoiceDTO {
	String getNameClient();

	String getCifClient();

	Integer getNumberInvoices();

	BigDecimal getTotalInvoices();

	BigDecimal getTotalInvoicesNoPaid();

	BigDecimal getTotalInvoicesPaid();

}
