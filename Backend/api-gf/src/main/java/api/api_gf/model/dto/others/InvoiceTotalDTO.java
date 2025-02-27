package api.api_gf.model.dto.others;

import java.math.BigDecimal;
import java.util.Date;

public interface InvoiceTotalDTO {

	String getNumInvoice();

	Date getDateInvoice();

	BigDecimal getTotalInvoice();

}
