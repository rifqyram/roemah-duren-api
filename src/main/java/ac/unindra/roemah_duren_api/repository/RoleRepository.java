package ac.unindra.roemah_duren_api.repository;

import ac.unindra.roemah_duren_api.constant.UserRole;
import ac.unindra.roemah_duren_api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByRole(UserRole role);
}
