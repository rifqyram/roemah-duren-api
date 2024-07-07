package ac.unindra.roemah_duren_api.entity;

import ac.unindra.roemah_duren_api.constant.ConstantTable;
import ac.unindra.roemah_duren_api.dto.response.CustomerResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.util.StringUtils;

@Entity
@Table(name = ConstantTable.CUSTOMER)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Customer extends BaseEntity {
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "address", length = 100)
    private String address;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "mobile_phone_no", length = 15)
    private String mobilePhoneNo;

    public CustomerResponse toResponse() {
        return CustomerResponse.builder()
                .id(getId())
                .name(name)
                .address(address)
                .email(email)
                .mobilePhoneNo(StringUtils.hasText(mobilePhoneNo) ? "0" + mobilePhoneNo : null)
                .build();
    }
}
