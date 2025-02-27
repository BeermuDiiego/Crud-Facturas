package api.api_gf.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invoice", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "id_user", "id_business", "num_invoice" }) })
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_invoice")
	private Integer invoiceId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_user", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_business", nullable = false)
	private Business business;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_client", nullable = false)
	private Client client;

	@Column(name = "num_invoice", nullable = false, length = 10)
	private String numInvoice;

	@Column(name = "date_invoice", nullable = false)
	private LocalDate dateInvoice;

	@Column(name = "address_work", nullable = false, length = 100)
	private String addressWork;

	@Column(name = "subtotal", nullable = false, precision = 11, scale = 2)
	private BigDecimal subtotal;

	@Column(name = "iva", nullable = false, precision = 5, scale = 2)
	private BigDecimal iva;

	@Column(name = "total", nullable = false, precision = 11, scale = 2)
	private BigDecimal total;

	@Column(name = "paid", nullable = false)
	private boolean paid;


	@OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<InvoiceItem> invoicesItems;
}
