package ac.unindra.roemah_duren_api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockResponse {
    private String id;
    private ProductResponse product;
    private Integer stock;
    private BranchResponse branch;
    private String updatedDate;
    private List<TransactionDetailResponse> transactionDetails;
}
