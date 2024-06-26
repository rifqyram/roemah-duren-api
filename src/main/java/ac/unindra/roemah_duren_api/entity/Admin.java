package ac.unindra.roemah_duren_api.entity;

import ac.unindra.roemah_duren_api.constant.ConstantTable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = ConstantTable.ADMIN)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Admin extends BaseEntity {
    private String nip;
    private String name;
    private String mobilePhoneNo;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_account_id", unique = true, nullable = false)
    private UserAccount userAccount;
}
