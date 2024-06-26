package ac.unindra.roemah_duren_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetailResponse {
    private String id;
    private String transactionId;
    private StockResponse stock;
    private Integer qty;
    private Long price;
}
