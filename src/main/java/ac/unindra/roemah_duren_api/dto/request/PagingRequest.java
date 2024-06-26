package ac.unindra.roemah_duren_api.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingRequest {
    private Integer size;
    private Integer page;
    private String query;
}
