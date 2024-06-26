package ac.unindra.roemah_duren_api.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminResponse {
    private String id;
    private String nip;
    private String name;
    private String email;
    private String mobilePhoneNo;
    private boolean status;
}
