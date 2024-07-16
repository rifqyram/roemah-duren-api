package ac.unindra.roemah_duren_api.service.impl;

import ac.unindra.roemah_duren_api.dto.response.UserResponse;
import ac.unindra.roemah_duren_api.entity.UserAccount;
import ac.unindra.roemah_duren_api.repository.UserAccountRepository;
import ac.unindra.roemah_duren_api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Start loadUserByUsername : {}", System.currentTimeMillis());
        UserAccount userAccount = userAccountRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
        log.info("End loadUserByUsername : {}", System.currentTimeMillis());
        return userAccount;
    }

    @Transactional(readOnly = true)
    @Override
    public UserAccount getByUserId(String id) {
        log.info("Start getByUserId : {}", System.currentTimeMillis());
        UserAccount userAccount = userAccountRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        log.info("End getByUserId : {}", System.currentTimeMillis());
        return userAccount;
    }

    @Override
    public UserAccount getByContext() {
        log.info("Start getByContext : {}", System.currentTimeMillis());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = userAccountRepository.findByEmail(authentication.getPrincipal().toString())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        log.info("End getByContext : {}", System.currentTimeMillis());
        return userAccount;
    }

    @Override
    public UserResponse getUserInfoByContext() {
        log.info("Start getUserByContext: {}", System.currentTimeMillis());
        UserAccount userAccount = getByContext();
        UserResponse response = userAccount.toResponse();
        log.info("End getUserByContext: {}", System.currentTimeMillis());
        return response;
    }


}
