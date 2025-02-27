package api.api_gf.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import api.api_gf.model.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);

	@Query("SELECT u.userId FROM User u WHERE u.email = :email")
	Integer findIdUserByEmail(@Param("email") String email);

}
