package ac.unindra.roemah_duren_api.entity;

import ac.unindra.roemah_duren_api.constant.ConstantTable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Entity
@Table(name = ConstantTable.STOCK)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Stock extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private Integer stock;
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
    private Date updatedDate;

    @PrePersist
    public void prePersist() {
        updatedDate = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        if (updatedDate != null) {
            updatedDate = new Date();
        }
    }
}
