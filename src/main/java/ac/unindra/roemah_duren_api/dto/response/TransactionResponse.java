package ac.unindra.roemah_duren_api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private String id;
    private CustomerResponse customer;
    private BranchResponse branch;
    private BranchResponse targetBranch;
    private List<TransactionDetailResponse> transactionDetails;
    private String transDate;
    private String transactionType;
    private Long totalPrice;
}
