package ac.unindra.roemah_duren_api.service.impl;

import ac.unindra.roemah_duren_api.constant.UserRole;
import ac.unindra.roemah_duren_api.dto.request.AuthRequest;
import ac.unindra.roemah_duren_api.dto.response.LoginResponse;
import ac.unindra.roemah_duren_api.dto.response.RegisterResponse;
import ac.unindra.roemah_duren_api.entity.Role;
import ac.unindra.roemah_duren_api.entity.UserAccount;
import ac.unindra.roemah_duren_api.repository.UserAccountRepository;
import ac.unindra.roemah_duren_api.service.AuthService;
import ac.unindra.roemah_duren_api.service.JwtService;
import ac.unindra.roemah_duren_api.service.RoleService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserAccountRepository userAccountRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerAdmin(AuthRequest request) {
        log.info("Start register admin: {}", System.currentTimeMillis());
        Role roleAdmin = roleService.getOrSave(UserRole.ROLE_ADMIN);

        String hashPassword = passwordEncoder.encode(request.getPassword());

        UserAccount account = UserAccount.builder()
                .email(request.getEmail().toLowerCase())
                .password(hashPassword)
                .role(List.of(roleAdmin))
                .isEnable(true)
                .build();

        userAccountRepository.saveAndFlush(account);

        List<String> roles = account.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        log.info("End register admin: {}", System.currentTimeMillis());
        return RegisterResponse.builder()
                .email(account.getEmail())
                .roles(roles)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public LoginResponse login(AuthRequest request) {
        log.info("Start login: {}", System.currentTimeMillis());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getEmail().toLowerCase(),
                request.getPassword()
        );
        Authentication authenticate = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserAccount userAccount = (UserAccount) authenticate.getPrincipal();
        String token = jwtService.generateToken(userAccount);
        log.info("End login: {}", System.currentTimeMillis());
        return LoginResponse.builder()
                .email(userAccount.getEmail())
                .roles(userAccount.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .token(token)
                .build();
    }
}
