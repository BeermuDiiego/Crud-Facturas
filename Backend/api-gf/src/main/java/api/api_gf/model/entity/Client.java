package api.api_gf.model.entity;

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
@Table(name = "client", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "id_user", "id_business", "cif_client" }) })
public class Client {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_client")
	private Integer clientId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_user", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_business", nullable = false)
	private Business business;

	@Column(name = "name_client", nullable = false, length = 150)
	private String nameClient;

	@Column(name = "cif_client", nullable = false, length = 9)
	private String cifClient;

	@Column(name = "address", nullable = false, length = 150)
	private String address;

	@Column(name = "email", length = 150)
	private String email;

	@OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Invoice> invoices;
}
