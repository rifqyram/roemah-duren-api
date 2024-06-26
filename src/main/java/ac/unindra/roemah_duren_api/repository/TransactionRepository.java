package ac.unindra.roemah_duren_api.repository;

import ac.unindra.roemah_duren_api.constant.TransactionType;
import ac.unindra.roemah_duren_api.entity.Transaction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.transDate >= :startOfDay AND t.transDate < :endOfDay")
    long countTransactionsToday(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    List<Transaction> findAllByTransDateBetweenAndBranch_IdAndTransactionType(LocalDateTime startDate, LocalDateTime endDate, String branchId, TransactionType transactionType);
    List<Transaction> findAllByTransDateBetweenAndTransactionType(LocalDateTime startDate, LocalDateTime endDate, TransactionType transactionType);


    static Specification<Transaction> hasSearchQuery(String q) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(q)) return criteriaBuilder.conjunction();
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("customer").get("name")), "%" + q.toLowerCase() + "%"),
                    criteriaBuilder.equal(root.get("branch").get("id"), q),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("branch").get("name")), "%" + q.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("targetBranch").get("name")), "%" + q.toLowerCase() + "%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("transactionType")), "%" + q.toLowerCase() + "%")
            );
        };
    }

    static Specification<Transaction> searchTransactionPerDay() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.function("date", String.class, root.get("transDate")), criteriaBuilder.function("date", String.class, criteriaBuilder.currentDate()));
    }
}
