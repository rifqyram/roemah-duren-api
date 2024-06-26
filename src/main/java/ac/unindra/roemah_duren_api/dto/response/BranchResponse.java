package ac.unindra.roemah_duren_api.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchResponse {
    private String id;
    private String code;
    private String name;
    private String address;
    private String mobilePhoneNo;
    private List<StockResponse> stocks;
}
