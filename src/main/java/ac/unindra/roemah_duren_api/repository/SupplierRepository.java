package ac.unindra.roemah_duren_api.repository;

import ac.unindra.roemah_duren_api.entity.Supplier;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, String>, JpaSpecificationExecutor<Supplier> {
    static Specification<Supplier> hasSearchQuery(String query) {
        return (root, query1, criteriaBuilder) -> {
            if (!StringUtils.hasText(query)) return criteriaBuilder.conjunction();

            var name = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + query.toLowerCase() + "%");
            var address = criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%" + query.toLowerCase() + "%");
            var email = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + query.toLowerCase() + "%");
            var mobilePhoneNo = criteriaBuilder.like(criteriaBuilder.lower(root.get("mobilePhoneNo")), "%" + query.toLowerCase() + "%");

            return criteriaBuilder.or(name, address, email, mobilePhoneNo);
        };
    }
}
