package api.api_gf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import api.api_gf.model.dto.ClientDTO;
import api.api_gf.model.entity.Client;
import api.api_gf.model.entity.Invoice;

public interface ClientRepository extends JpaRepository<Client, Integer> {

	boolean existsByBusinessBusinessIdAndCifClient(Integer businessId, String cifClient);

	boolean existsByUserIdAndBusinessBusinessIdAndClientId(Integer userId, Integer businessId, Integer clientId);

	List<Client> findByUserIdAndBusinessBusinessId(Integer userId, Integer businessId);

	void deleteByClientId(Integer clientId);

	boolean existsByClientId(Integer clientId);

	Client findByClientId(Integer clientId);

	@Query(value = "SELECT COUNT(c.id_client) FROM client c WHERE c.id_business = :businessId AND c.cif_client = :cifClient AND c.id_client != :clientId", nativeQuery = true)
	Integer existsByBusinessBusinessIdAndCifClientAndNotClientId(@Param("businessId") Integer businessId,
			@Param("cifClient") String cifClient, @Param("clientId") Integer clientId);

	Client findByUserIdAndBusinessBusinessIdAndClientId(Integer userIdAuth, Integer businessId, Integer clientId);

}
