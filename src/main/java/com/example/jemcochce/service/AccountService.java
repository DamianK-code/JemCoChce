package com.example.jemcochce.service;

import com.example.jemcochce.exeption.InvalidRegisterData;
import com.example.jemcochce.model.Account;
import com.example.jemcochce.model.AccountRole;
import com.example.jemcochce.model.CreateAccountRequest;
import com.example.jemcochce.model.RolesDto;
import com.example.jemcochce.repository.AccountRepository;
import com.example.jemcochce.repository.AccountRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.jemcochce.configuration.DataInitializer.ROLE_ADMIN;
import static com.example.jemcochce.configuration.DataInitializer.ROLE_USER;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountRoleRepository accountRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Account> getAccountList() {
        return accountRepository.findAll();
    }


    public boolean register(CreateAccountRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidRegisterData("Potwierdź tym samym hasłem!");
        }
        Optional<Account> optionalAccount = accountRepository.findByUsername(request.getUsername());
        if (optionalAccount.isPresent()) {
            throw new InvalidRegisterData("Użytkownik o podanym loginie już istnieje!");
        }
        Account account = Account.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
        accountRepository.save(account);
        return true;
    }

    public boolean deleteAccount(Long accountId) {
        if (accountRepository.existsById(accountId)) {
            accountRepository.deleteById(accountId);
            return true;
        }
        return false;
    }

    public Optional<Account> findAccount(Long accountId) {
        return accountRepository.findById(accountId);
    }

    public void updateAccount(Account account, RolesDto rolesDto) {
        Optional<Account> accountOptional = accountRepository.findById(account.getId());
        if (accountOptional.isPresent()) {
            Account editedAcount = accountOptional.get();
            editedAcount.setEnabled(account.isEnabled());
            editedAcount.setAccountNonLocked(account.isAccountNonLocked());
            if (account.getPassword() != null && !account.getPassword().isEmpty()) {
                editedAcount.setPassword(passwordEncoder.encode(account.getPassword()));
            }
            checkAndUpdateRole(editedAcount, ROLE_ADMIN, rolesDto.isAdmin());
            checkAndUpdateRole(editedAcount, ROLE_USER, rolesDto.isUser());
        }
    }

    private void checkAndUpdateRole(Account editedAccount, String roleName, boolean shouldHaveAuthority) {
        if (editedAccount.getRoles().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getName().equals(roleName))) {
            if (!shouldHaveAuthority) {
                Optional<AccountRole> optionalAccountRole = accountRoleRepository.findByName(roleName);
                if (optionalAccountRole.isPresent()) {
                    AccountRole accountRole = optionalAccountRole.get();
                    editedAccount.getRoles().remove(accountRole);
                }
            }
            return;
        }
        if (shouldHaveAuthority) {
            Optional<AccountRole> optionalAccountRole = accountRoleRepository.findByName(roleName);
            if (optionalAccountRole.isPresent()) {
                AccountRole accountRole = optionalAccountRole.get();
                editedAccount.getRoles().add(accountRole);
            }
        }
    }
}
