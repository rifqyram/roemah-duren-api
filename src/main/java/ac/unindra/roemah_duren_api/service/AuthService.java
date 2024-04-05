package ac.unindra.roemah_duren_api.service;


import ac.unindra.roemah_duren_api.dto.request.AuthRequest;
import ac.unindra.roemah_duren_api.dto.response.LoginResponse;
import ac.unindra.roemah_duren_api.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerAdmin(AuthRequest request);
    LoginResponse login(AuthRequest request);
}
