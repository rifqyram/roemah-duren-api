package ac.unindra.roemah_duren_api.entity;

import ac.unindra.roemah_duren_api.constant.ConstantTable;
import ac.unindra.roemah_duren_api.dto.response.SupplierResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = ConstantTable.SUPPLIER)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Supplier extends BaseEntity {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "address", nullable = false, length = 100)
    private String address;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "mobile_phone_no", nullable = false, length = 15, unique = true)
    private String mobilePhoneNo;

    public SupplierResponse toResponse() {
        return SupplierResponse.builder()
                .id(getId())
                .name(name)
                .address(address)
                .email(email)
                .mobilePhoneNo(mobilePhoneNo)
                .build();
    }
}
