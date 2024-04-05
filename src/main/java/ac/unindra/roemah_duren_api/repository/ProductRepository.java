package ac.unindra.roemah_duren_api.repository;

import ac.unindra.roemah_duren_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
}
