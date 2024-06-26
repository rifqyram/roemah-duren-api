package ac.unindra.roemah_duren_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequest {
    private String id;
    private String name;
    private String address;
    private String email;
    private String mobilePhoneNo;
}
