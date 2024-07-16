package ac.unindra.roemah_duren_api.service.impl;

import ac.unindra.roemah_duren_api.constant.ResponseMessage;
import ac.unindra.roemah_duren_api.constant.UserRole;
import ac.unindra.roemah_duren_api.dto.request.AdminRequest;
import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.response.AdminResponse;
import ac.unindra.roemah_duren_api.entity.Admin;
import ac.unindra.roemah_duren_api.entity.Role;
import ac.unindra.roemah_duren_api.entity.UserAccount;
import ac.unindra.roemah_duren_api.repository.AdminRepository;
import ac.unindra.roemah_duren_api.repository.UserAccountRepository;
import ac.unindra.roemah_duren_api.service.AdminService;
import ac.unindra.roemah_duren_api.service.RoleService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UserAccountRepository userAccountRepository;

    @Value("${app.roemah_duren.email.admin}")
    private String ADMIN_EMAIL;
    @Value("${app.roemah_duren.password.password}")
    private String ADMIN_PASSWORD;

    @Value("${app.roemah_duren.email.owner}")
    private String OWNER_EMAIL;
    @Value("${app.roemah_duren.password.owner}")
    private String OWNER_PASSWORD;

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void initSuperAdmin() {
        log.info("Start init admin: {}", System.currentTimeMillis());
        Optional<Admin> currentUser = adminRepository.findByUserAccount_Email(ADMIN_EMAIL);
        if (currentUser.isPresent()) return;

        Role admin = roleService.getOrSave(UserRole.ROLE_ADMIN);

        UserAccount account = UserAccount.builder()
                .email(ADMIN_EMAIL.toLowerCase())
                .password(passwordEncoder.encode(ADMIN_PASSWORD))
                .role(List.of(admin))
                .isEnable(true)
                .build();

        Admin adminModel = Admin.builder()
                .userAccount(account)
                .build();
        adminRepository.saveAndFlush(adminModel);
        log.info("End init admin: {}", System.currentTimeMillis());
    }

    @Transactional(rollbackFor = Exception.class)
    @PostConstruct
    public void initOwner() {
        log.info("Start init owner: {}", System.currentTimeMillis());
        Optional<Admin> currentUser = adminRepository.findByUserAccount_Email(OWNER_EMAIL);
        if (currentUser.isPresent()) return;

        Role owner = roleService.getOrSave(UserRole.ROLE_OWNER);

        UserAccount account = UserAccount.builder()
                .email(OWNER_EMAIL.toLowerCase())
                .password(passwordEncoder.encode(OWNER_PASSWORD))
                .role(List.of(owner))
                .isEnable(true)
                .build();
        userAccountRepository.saveAndFlush(account);

        log.info("End init owner: {}", System.currentTimeMillis());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AdminResponse create(AdminRequest request) {
        log.info("Start create Admin: {}", System.currentTimeMillis());
        Role role = roleService.getOrSave(UserRole.ROLE_ADMIN);
        Admin admin = Admin.builder()
                .nip(request.getNip())
                .name(request.getName())
                .mobilePhoneNo(request.getMobilePhoneNo())
                .userAccount(UserAccount.builder()
                        .email(request.getEmail().toLowerCase())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .isEnable(request.isStatus())
                        .role(List.of(role))
                        .build())
                .build();
        adminRepository.saveAndFlush(admin);
        return toResponse(admin);
    }

    @Transactional(readOnly = true)
    @Override
    public AdminResponse getOne(String id) {
        log.info("Start getOne Admin: {}", System.currentTimeMillis());
        Admin admin = getById(id);
        log.info("End getOne Admin: {}", System.currentTimeMillis());
        return toResponse(admin);
    }

    @Override
    @Transactional
    public Admin findByContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        return adminRepository.findByUserAccount_Email(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.SUCCESS_GET_DATA));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AdminResponse> getPage(PagingRequest request) {
        log.info("Start getPage Admin: {}", System.currentTimeMillis());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Specification<Admin> specification = Specification.where(AdminRepository.hasSearchQuery(request.getQuery()));
        Page<AdminResponse> adminResponses = adminRepository.findAll(specification, pageable).map(this::toResponse);
        log.info("End getPage Admin: {}", System.currentTimeMillis());
        return adminResponses;
    }

    @Transactional(readOnly = true)
    @Override
    public List<AdminResponse> getAll() {
        log.info("Start getAll Admin: {}", System.currentTimeMillis());
        List<AdminResponse> adminResponses = adminRepository.findAll().stream().map(this::toResponse).toList();
        log.info("End getAll Admin: {}", System.currentTimeMillis());
        return adminResponses;
    }

    @Transactional
    @Override
    public Admin getById(String id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public AdminResponse update(AdminRequest request) {
        log.info("Start update Admin: {}", System.currentTimeMillis());
        Admin admin = getById(request.getId());
        admin.setNip(request.getNip());
        admin.setName(request.getName());
        admin.setMobilePhoneNo(request.getMobilePhoneNo());
        admin.getUserAccount().setPassword(passwordEncoder.encode(request.getPassword()));
        admin.getUserAccount().setEmail(request.getEmail().toLowerCase());
        admin.getUserAccount().setIsEnable(request.isStatus());
        log.info("End update Admin: {}", System.currentTimeMillis());
        return toResponse(admin);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        log.info("Start delete Admin: {}", System.currentTimeMillis());
        Admin admin = getById(id);
        adminRepository.delete(admin);
        log.info("End delete Admin: {}", System.currentTimeMillis());
    }

    private AdminResponse toResponse(Admin admin) {
        return AdminResponse.builder()
                .id(admin.getId())
                .nip(admin.getNip())
                .name(admin.getName())
                .email(admin.getUserAccount().getEmail())
                .mobilePhoneNo(admin.getMobilePhoneNo() != null ? "0" + admin.getMobilePhoneNo() : null)
                .status(admin.getUserAccount().getIsEnable())
                .build();
    }
}
