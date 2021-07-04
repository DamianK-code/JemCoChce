package com.example.jemcochce.controller;

import com.example.jemcochce.model.Account;
import com.example.jemcochce.service.PrzepisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class PrzepisController {
    private final PrzepisService przepisService;

    @GetMapping("/przepis")
    public String getPrzepisIndexPage(Model model) {
        model.addAttribute("przepisy", przepisService.getAllPrzepisy());
        model.addAttribute("skladniki", przepisService.getAllSkladniki());
        return "recipe-index";
    }

    @GetMapping("/przepis/add")
    public String getPrzepisForm() {
        return "add-recipe";
    }

    @GetMapping("/przepis/skladnik/add")
    public String addSkladnikiToPrzepisForm(Model model, @RequestParam Long przepisId) {
        model.addAttribute("wszystkie_dostepne_skladniki", przepisService.getAllSkladniki());
        model.addAttribute("id_przepisu", przepisId);
        return "add-ingr-to-recipe";
    }

    @PostMapping("/przepis/skladnik/add")
    public String submitSkladnikiToPrzepisForm(Long przepis_id, Long skladnik_id, double ilosc) {
        przepisService.addSkladnikToPrzepis(przepis_id, skladnik_id, ilosc);
        return "redirect:/przepis";
    }

    @PostMapping("przepis/add")
    public String submitPrzepisForm(String name, Principal principal) {
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) principal;
            if (usernamePasswordAuthenticationToken.getPrincipal() instanceof Account) {
                Account account = (Account) usernamePasswordAuthenticationToken.getPrincipal();
                przepisService.addPrzepis(name, account);
            }
        }
        return "redirect:/przepis";
    }

    @GetMapping("/skladnik/add")
    public String getSkladnikForm() {
        return "add-ingredient";
    }

    @PostMapping("/skladnik/add")
    public String submitSkladnikForm(String name) {
        przepisService.addSkladnik(name);
        return "redirect:/skladnik";
    }
}
