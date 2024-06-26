package ac.unindra.roemah_duren_api.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductRequest {
    private String id;
    private String code;
    private String name;
    private Long price;
    private String description;
    private String supplierId;
}
