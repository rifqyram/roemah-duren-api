package ac.unindra.roemah_duren_api.entity;

import ac.unindra.roemah_duren_api.constant.ConstantTable;
import ac.unindra.roemah_duren_api.dto.response.TransactionDetailResponse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = ConstantTable.TRANSACTION_DETAIL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class TransactionDetail extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    @JsonBackReference
    private Stock stock;

    @Column(name = "qty", nullable = false)
    private Integer qty;

    @Column(name = "price", nullable = false)
    private Long price;

    public TransactionDetailResponse toResponse() {
        return TransactionDetailResponse.builder()
                .id(getId())
                .transactionId(transaction.getId())
                .stock(stock.toResponse())
                .qty(qty)
                .price(price)
                .build();
    }
}
