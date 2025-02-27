package api.api_gf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import api.api_gf.model.dto.others.BusinessTotalInvoiceDTO;
import api.api_gf.model.dto.others.BusinessTotalInvoiceDayDTO;
import api.api_gf.model.dto.others.BusinessTotalInvoiceMonthDTO;
import api.api_gf.model.dto.others.BusinessTotalInvoiceYearDTO;
import api.api_gf.model.dto.others.ClientTotalInvoiceDTO;
import api.api_gf.model.dto.others.InvoiceTotalDTO;
import api.api_gf.model.entity.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

	boolean existsByClientClientIdAndNumInvoice(Integer clientId, String numInvoice);

	boolean existsByInvoiceId(Integer invoiceId);

	List<Invoice> findByUserIdAndBusinessBusinessId(Integer userId, Integer businessId);

	void deleteByInvoiceId(Integer invoiceId);

	@Query(value = "SELECT g.numInvoice, g.dateInvoice, g.totalInvoice " + "FROM get_list_top_invoices g "
			+ "WHERE g.businessId = :businessId AND g.userId = :userId", nativeQuery = true)
	List<InvoiceTotalDTO> findTotalInvoices(@Param("userId") Integer userId, @Param("businessId") Integer businessId);

	@Query(value = "SELECT g.totalPrices, g.totalPricesNoPaid, g.totalPricesPaid, g.numClients,g.numInvoices "
			+ "FROM get_total_prices_business g "
			+ "WHERE g.businessId = :businessId AND g.userId = :userId", nativeQuery = true)
	List<BusinessTotalInvoiceDTO> findTotalPricesByBusiness(@Param("businessId") Integer businessId,
			@Param("userId") Integer userId);

	@Query(value = "SELECT g.totalPrices,g.totalPricesNoPaid,g.totalPricesPaid,g.invoiceDay "
			+ "FROM get_total_prices_business_day g "
			+ "WHERE g.businessId = :businessId AND g.userId = :userId", nativeQuery = true)
	List<BusinessTotalInvoiceDayDTO> findTotalPricesByBusinessDay(@Param("businessId") Integer businessId,
			@Param("userId") Integer userId);

	@Query(value = "SELECT g.totalPrices,g.totalPricesNoPaid,g.totalPricesPaid,g.invoiceMonth "
			+ "FROM get_total_prices_business_month g "
			+ "WHERE g.businessId = :businessId AND g.userId = :userId", nativeQuery = true)
	List<BusinessTotalInvoiceMonthDTO> findTotalPricesByBusinessMonth(@Param("businessId") Integer businessId,
			@Param("userId") Integer userId);

	@Query(value = "SELECT g.totalPrices,g.totalPricesNoPaid,g.totalPricesPaid,g.invoiceYear "
			+ "FROM get_total_prices_business_year g "
			+ "WHERE g.businessId = :businessId AND g.userId = :userId", nativeQuery = true)
	List<BusinessTotalInvoiceYearDTO> findTotalPricesByBusinessYear(@Param("businessId") Integer businessId,
			@Param("userId") Integer userId);

	@Query(value = "SELECT g.nameClient,g.cifClient,g.totalInvoices,g.totalInvoicesNoPaid,g.totalInvoicesPaid,g.numberInvoices "
			+ "FROM get_total_prices_client g "
			+ "WHERE g.businessId = :businessId AND g.userId = :userId", nativeQuery = true)
	List<ClientTotalInvoiceDTO> findTotalPricesClients(@Param("businessId") Integer businessId,
			@Param("userId") Integer userId);

}
