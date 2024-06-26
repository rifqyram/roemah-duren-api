package ac.unindra.roemah_duren_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse {
    private int totalPages;
    private long totalElements;
    private int page;
    private int size;
    private boolean hasNext;
    private boolean hasPrevious;
}
