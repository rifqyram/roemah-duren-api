package ac.unindra.roemah_duren_api.entity;

import ac.unindra.roemah_duren_api.constant.ConstantTable;
import ac.unindra.roemah_duren_api.dto.response.StockResponse;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.cglib.core.Local;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

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

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "stock")
    @JsonManagedReference
    private List<TransactionDetail> transactionDetails;

    @PrePersist
    public void prePersist() {
        updatedDate = LocalDateTime.now();
    }

    public StockResponse toResponse() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = updatedDate.format(dateTimeFormatter);

        Branch branch = Branch.builder()
                .id(this.branch.id)
                .name(this.branch.getName())
                .address(this.branch.getAddress())
                .mobilePhoneNo(this.branch.getMobilePhoneNo())
                .build();

        return StockResponse.builder()
                .id(super.getId())
                .product(product.toResponse())
                .stock(stock)
                .branch(branch.toResponse())
                .updatedDate(date)
                .build();
    }
}
