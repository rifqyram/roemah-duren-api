package ac.unindra.roemah_duren_api.entity;

import ac.unindra.roemah_duren_api.constant.ConstantTable;
import ac.unindra.roemah_duren_api.constant.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = ConstantTable.TRANSACTION)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Transaction extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "target_branch_id")
    private Branch targetBranch;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL)
    private List<TransactionDetail> transactionDetails;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;
}
