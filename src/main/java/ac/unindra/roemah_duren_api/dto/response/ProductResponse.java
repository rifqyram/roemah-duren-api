package ac.unindra.roemah_duren_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private String id;
    private String code;
    private String name;
    private Long price;
    private String description;
    private SupplierResponse supplier;
}
