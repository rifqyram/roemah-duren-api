package ac.unindra.roemah_duren_api.repository;

import ac.unindra.roemah_duren_api.entity.Branch;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public interface BranchRepository extends JpaRepository<Branch, String>, JpaSpecificationExecutor<Branch> {
    static Specification<Branch> hasSearchQuery(String q) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(q)) return criteriaBuilder.conjunction();

            var codeLikePredicate = criteriaBuilder.like(root.get("code"), "%" + q + "%");
            var nameLowerLikePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + q.toLowerCase() + "%");
            var addressLowerLikePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), "%" + q.toLowerCase() + "%");
            var mobilePhoneNoLowerLikePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("mobilePhoneNo")), "%" + q.toLowerCase() + "%");

            return criteriaBuilder.or(codeLikePredicate, nameLowerLikePredicate, addressLowerLikePredicate, mobilePhoneNoLowerLikePredicate);
        };
    }
}
