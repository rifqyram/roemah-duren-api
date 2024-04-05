package ac.unindra.roemah_duren_api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private String customerId;
    private String branchId;
    private List<TransactionDetailResponse> transactionDetails;
    private Long totalPrice;
}
