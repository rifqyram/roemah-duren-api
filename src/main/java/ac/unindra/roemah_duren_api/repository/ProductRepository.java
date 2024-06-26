package ac.unindra.roemah_duren_api.repository;

import ac.unindra.roemah_duren_api.entity.Product;
import ac.unindra.roemah_duren_api.entity.Supplier;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    static Specification<Product> hasSearchQuery(String q) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(q)) return criteriaBuilder.conjunction();
            List<Predicate> predicates = new ArrayList<>();

            var code = criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), "%" + q.toLowerCase() + "%");
            predicates.add(code);

            var name = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + q.toLowerCase() + "%");
            predicates.add(name);

            if (q.matches("^\\d+$")) {
                var price = criteriaBuilder.equal(root.get("price"), Long.parseLong(q));
                predicates.add(price);
            }

            var description = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + q.toLowerCase() + "%");
            predicates.add(description);

            var supplierName = criteriaBuilder.like(criteriaBuilder.lower(root.get("supplier").get("name")), "%" + q.toLowerCase() + "%");
            predicates.add(supplierName);

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }
}
