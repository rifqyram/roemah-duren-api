package ac.unindra.roemah_duren_api.repository;

import ac.unindra.roemah_duren_api.entity.Admin;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String>, JpaSpecificationExecutor<Admin> {
    static Specification<Admin> hasSearchQuery(String q) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(q)) return criteriaBuilder.conjunction();
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("nip")), "%" + q.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + q.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("mobilePhoneNo")), "%" + q.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("userAccount").get("email")), "%" + q.toLowerCase() + "%")
            );
        };
    }
}
