package ac.unindra.roemah_duren_api.dto.request;

import ac.unindra.roemah_duren_api.constant.ConstantTable;
import ac.unindra.roemah_duren_api.entity.BaseEntity;
import ac.unindra.roemah_duren_api.entity.Branch;
import ac.unindra.roemah_duren_api.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockRequest {
    private String id;
    private String productId;
    private Integer stock;
    private String branchId;
}
