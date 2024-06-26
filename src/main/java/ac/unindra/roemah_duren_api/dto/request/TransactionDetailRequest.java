package ac.unindra.roemah_duren_api.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetailRequest {
    private String stockId;
    private Long price;
    private Integer qty;
}
