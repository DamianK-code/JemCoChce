package com.example.jemcochce.controller;

import com.example.jemcochce.model.Skladnik;
import com.example.jemcochce.service.SkladnikService;
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
    private final SkladnikService skladnikService;

    @GetMapping("/add")
    public String addSkladnikForm(Model model){
        Skladnik skladnik = new Skladnik();
        model.addAttribute("nowy_skladnik", skladnik);
        return "skladnik-add";
    }

    @PostMapping("/add")
    public String addSkladnik(Skladnik skladnik){
        skladnikService.addSkladnik(skladnik);
        return "redirect:/skladnik";
    }



}
