package ac.unindra.roemah_duren_api.service.impl;

import ac.unindra.roemah_duren_api.constant.UserRole;
import ac.unindra.roemah_duren_api.entity.Role;
import ac.unindra.roemah_duren_api.repository.RoleRepository;
import ac.unindra.roemah_duren_api.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Role getOrSave(UserRole role) {
        log.info("getOrSave role: {}", System.currentTimeMillis());
        return roleRepository.findByRole(role)
                .orElseGet(() -> roleRepository.saveAndFlush(Role.builder().role(role).build()));
    }
}
