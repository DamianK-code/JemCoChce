package com.example.jemcochce.controller;

import com.example.jemcochce.model.Account;
import com.example.jemcochce.model.RolesDto;
import com.example.jemcochce.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

import static com.example.jemcochce.configuration.DataInitializer.ROLE_ADMIN;
import static com.example.jemcochce.configuration.DataInitializer.ROLE_USER;

@Slf4j
@Controller
@RequestMapping("/admin")
@PreAuthorize(value = "hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final AccountService accountService;

    @GetMapping
    public String getIndex() {
        return "admin-index";
    }

    @ModelAttribute
    public void addAtributes(Model model, Principal principal) {
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
            if (usernamePasswordAuthenticationToken.getPrincipal() instanceof Account) {
                Account account = (Account) usernamePasswordAuthenticationToken.getPrincipal();
                model.addAttribute("username", account.getUsername());
            }
        }
    }

    @GetMapping("/accounts")
    public String getAccounts(Model model, @RequestParam(required = false) String error) {
        model.addAttribute("accounts", accountService.getAccountList());
        model.addAttribute("error_msg", error);
        return "admin-account-list";
    }

    @GetMapping("/account/delete/{accountId}")
    public String deleteAccount(Model model, @PathVariable Long accountId, HttpServletRequest request) {
        boolean success = accountService.deleteAccount(accountId);
        if (success) {
            return "redirect:" + request.getHeader("referer");
        }
        return "redirect:/accounts?error=Unable do delete account";
    }

    @GetMapping("/account/edit/{accountId}")
    public String editAccount(Model model, @PathVariable Long accountId) {
        Optional<Account> optionalAccount = accountService.findAccount(accountId);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            boolean isUser = account.getRoles().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getName().equals(ROLE_USER));
            boolean isAdmin = account.getRoles().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getName().equals(ROLE_ADMIN));

            model.addAttribute("role_admin", isAdmin);
            model.addAttribute("role_user", isUser);
            model.addAttribute("account_to_edit", optionalAccount.get());
            return "admin-account-edit";
        }
        return "redirect:/accounts?error=Unable to delete account";
    }

    @PostMapping("/account/edit")
    public String submitAccount(Account account,
                                boolean admin,
                                boolean user) {
        accountService.updateAccount(account, new RolesDto(admin, user));
        return "redirect:/admin/accounts";
    }
}
