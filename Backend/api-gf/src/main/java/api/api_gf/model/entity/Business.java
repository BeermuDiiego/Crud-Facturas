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
@Table(name = "business", uniqueConstraints = { @UniqueConstraint(columnNames = { "id_user", "cif_business" }) })
public class Business {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_business")
	private Integer businessId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_user", nullable = false)
	private User user;

	@Column(name = "name_business", nullable = false, length = 150)
	private String nameBusiness;

	@Column(name = "cif_business", nullable = false, length = 9)
	private String cifBusiness;

	@Column(name = "email", nullable = false, length = 150)
	private String email;

	@Column(name = "address", nullable = false, length = 150)
	private String address;

	@Column(name = "phone", length = 20)
	private String phone;

	@Column(name = "account_number", nullable = false, length = 50)
	private String accountNumber;

	@OneToMany(mappedBy = "business", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Client> clients;

}