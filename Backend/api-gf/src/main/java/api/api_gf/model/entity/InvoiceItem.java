package api.api_gf.model.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "invoice_items", uniqueConstraints = { @UniqueConstraint(columnNames = { "id_item", "id_invoice" }) })
public class InvoiceItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_item")
	private Integer itemId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_invoice")
	private Invoice invoice;

	@Column(name = "service_name", nullable = false, length = 150)
	private String serviceName;

	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	@Column(name = "price", nullable = false, precision = 11, scale = 2)
	private BigDecimal price;

	@Column(name = "total", nullable = false, precision = 11, scale = 2)
	private BigDecimal total;
}
