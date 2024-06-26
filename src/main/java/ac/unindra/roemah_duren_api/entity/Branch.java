package ac.unindra.roemah_duren_api.entity;

import ac.unindra.roemah_duren_api.constant.ConstantTable;
import ac.unindra.roemah_duren_api.dto.response.BranchResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = ConstantTable.BRANCH)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Branch extends BaseEntity {
    @Column(name = "code", nullable = false, length = 10, unique = true)
    private String code;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "address", nullable = false, length = 100)
    private String address;

    @Column(name = "mobile_phone_no", nullable = false, length = 15, unique = true)
    private String mobilePhoneNo;

    @OneToMany(mappedBy = "branch")
    private List<Stock> stocks;

    public BranchResponse toResponse() {
        return BranchResponse.builder()
                .id(id)
                .name(name)
                .address(address)
                .mobilePhoneNo(mobilePhoneNo)
                .stocks(stocks != null && !stocks.isEmpty() ? stocks.stream().map(Stock::toResponse).collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }
}
