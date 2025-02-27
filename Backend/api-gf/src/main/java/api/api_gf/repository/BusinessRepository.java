package api.api_gf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import api.api_gf.model.entity.Business;

public interface BusinessRepository extends JpaRepository<Business, Integer> {

	boolean existsByUserIdAndCifBusiness(Integer userId, String cifBusiness);

	Business findByUserIdAndBusinessId(Integer userId, Integer businessId);

	boolean existsByBusinessId(Integer businessId);

	boolean existsByUserIdAndBusinessId(Integer userId, Integer businessId);

	List<Business> findByUserId(Integer userId);

	void deleteByBusinessId(Integer businessId);

	@Query(value = "SELECT CASE WHEN COUNT(id_business) > 0 THEN 1 ELSE 0 END FROM business b WHERE b.id_user = :userId AND b.cif_business = :cifBusiness AND b.id_business != :businessId", nativeQuery = true)
	Integer existsByUserIdAndCifBusinessAndNotBusinessId(@Param("userId") Integer userId,
			@Param("cifBusiness") String cifBusiness, @Param("businessId") Integer businessId);
}
