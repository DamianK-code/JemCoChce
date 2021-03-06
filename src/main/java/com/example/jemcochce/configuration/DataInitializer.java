package com.example.jemcochce.configuration;

import com.example.jemcochce.model.Account;
import com.example.jemcochce.model.AccountRole;
import com.example.jemcochce.repository.AccountRepository;
import com.example.jemcochce.repository.AccountRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ContextRefreshedEvent> {
    private final static String ADMIN_USERNAME = "admin";
    private final static String ADMIN_PASSWORD = "nimda";

    public final static String ROLE_ADMIN = "ROLE_ADMIN";
    public final static String ROLE_USER = "ROLE_USER";

    private final static String[] AVAILABLE_ROLES = {ROLE_ADMIN, ROLE_USER};

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        for (String role : AVAILABLE_ROLES) {
            addRole(role);
        }
        addUser(ADMIN_USERNAME, ADMIN_PASSWORD, AVAILABLE_ROLES);
        addUser("user","resu",new String[] {ROLE_USER});
    }

    private void addUser(String username, String password, String[] roles) {
        Optional<Account> optionalAccount = accountRepository.findByUsername(username);
        if (!optionalAccount.isPresent()) {
            Account account = Account.builder()
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .build();
            Set<AccountRole> roleSet = new HashSet<>();
            for (String role : roles) {
                Optional<AccountRole> optionalAccountRole = accountRoleRepository.findByName(role);
                if (optionalAccountRole.isPresent()){
                    AccountRole accountRole = optionalAccountRole.get();
                    roleSet.add(accountRole);
                }
            }
            account.setRoles(roleSet);
            accountRepository.save(account);
        }
    }

    private void addRole(String role) {
        Optional<AccountRole> optionalAccountRole = accountRoleRepository.findByName(role);
        if (!optionalAccountRole.isPresent()) {
            AccountRole accountRole = AccountRole.builder()
                    .name(role)
                    .build();
            accountRoleRepository.save(accountRole);
        }
    }
}
