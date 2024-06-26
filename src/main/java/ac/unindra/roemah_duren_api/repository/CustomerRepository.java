package ac.unindra.roemah_duren_api.repository;

import ac.unindra.roemah_duren_api.entity.Customer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer> {
    static Specification<Customer> hasSearchQuery(String q) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(q)) return criteriaBuilder.conjunction();

            var name = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + q.toLowerCase() + "%");
            var address = criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%" + q.toLowerCase() + "%");
            var email = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + q.toLowerCase() + "%");
            var phoneNumberNo = criteriaBuilder.like(criteriaBuilder.lower(root.get("mobilePhoneNo")), "%" + q.toLowerCase() + "%");

            return criteriaBuilder.or(name, address, email, phoneNumberNo);
        };
    }
}
