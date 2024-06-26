package ac.unindra.roemah_duren_api.security;

import ac.unindra.roemah_duren_api.dto.response.CommonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        CommonResponse<?> res = CommonResponse.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .message(getErrorMessage(authException))
                .build();
        String responseString = objectMapper.writeValueAsString(res);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(responseString);
    }

    private String getErrorMessage(AuthenticationException authException) {
        String errorMessage = authException.getMessage();

        if ("Bad credentials".equals(errorMessage)) {
            errorMessage = "Email atau Password tidak valid";
        } else if ("Full authentication is required to access this resource".equals(errorMessage)) {
            errorMessage = "Anda tidak diizinkan untuk mengakses halaman ini";
        } else if ("User is disabled".equalsIgnoreCase(errorMessage)) {
            errorMessage = "Akun Anda nonaktif, silahkan verifikasi akun Anda atau hubungi admin";
        }

        return errorMessage;
    }
}
