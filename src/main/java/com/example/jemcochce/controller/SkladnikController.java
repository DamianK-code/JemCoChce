package com.example.jemcochce.controller;

import com.example.jemcochce.model.Skladnik;
import com.example.jemcochce.service.PrzepisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/skladnik")
@RequiredArgsConstructor
public class SkladnikController {
    private final PrzepisService przepisService;

    @GetMapping("/add")
    public String addSkladnikForm(Model model){
        Skladnik skladnik = new Skladnik();
        model.addAttribute("nowySkladnik", skladnik);
        return "skladnik-add";
    }

    @PostMapping("/add")
    public String addSkladnik(String skladnik){
        przepisService.addSkladnik(skladnik);
        return "redirect:/skladnik";
    }



}
