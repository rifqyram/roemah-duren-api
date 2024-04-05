package ac.unindra.roemah_duren_api.service;


import ac.unindra.roemah_duren_api.dto.response.JwtClaims;
import ac.unindra.roemah_duren_api.entity.UserAccount;

public interface JwtService {
    String generateToken(UserAccount userAccount);
    boolean verifyJwtToken(String token);
    JwtClaims getClaimsByToken(String token);
}
