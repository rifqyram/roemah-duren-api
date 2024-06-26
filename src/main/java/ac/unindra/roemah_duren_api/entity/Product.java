package ac.unindra.roemah_duren_api.entity;

import ac.unindra.roemah_duren_api.constant.ConstantTable;
import ac.unindra.roemah_duren_api.dto.response.ProductResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = ConstantTable.PRODUCT)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Product extends BaseEntity {

    @Column(name = "code", unique = true, nullable = false, length = 20)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    public ProductResponse toResponse() {
        return ProductResponse.builder()
                .id(getId())
                .code(code)
                .name(name)
                .price(price)
                .description(description)
                .supplier(supplier.toResponse())
                .build();
    }
}
