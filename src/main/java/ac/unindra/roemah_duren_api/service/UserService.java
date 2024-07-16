package ac.unindra.roemah_duren_api.service;


import ac.unindra.roemah_duren_api.dto.response.UserResponse;
import ac.unindra.roemah_duren_api.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserAccount getByUserId(String id);
    UserAccount getByContext();
    UserResponse getUserInfoByContext();
}
