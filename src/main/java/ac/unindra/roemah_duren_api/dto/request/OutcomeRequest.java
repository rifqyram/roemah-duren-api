package ac.unindra.roemah_duren_api.dto.request;

import ac.unindra.roemah_duren_api.constant.ConstantTable;
import ac.unindra.roemah_duren_api.entity.BaseEntity;
import ac.unindra.roemah_duren_api.entity.Branch;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutcomeRequest {
    private String id;
    private Long amount;
    private String description;
    private String branchId;
}
