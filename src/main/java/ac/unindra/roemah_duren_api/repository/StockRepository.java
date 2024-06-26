package ac.unindra.roemah_duren_api.repository;

import ac.unindra.roemah_duren_api.entity.Branch;
import ac.unindra.roemah_duren_api.entity.Product;
import ac.unindra.roemah_duren_api.entity.Stock;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public interface StockRepository extends JpaRepository<Stock, String>, JpaSpecificationExecutor<Stock> {
    static <T> Specification<T> hasSearchQuery(String q) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(q)) return criteriaBuilder.conjunction();
            return criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("branch").get("id"), q),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("product").get("name")), "%" + q.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("branch").get("name")), "%" + q.toLowerCase() + "%")
            );
        };
    }

    boolean existsByProductAndBranch(Product product, Branch branch);
}
