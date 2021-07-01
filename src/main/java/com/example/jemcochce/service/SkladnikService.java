package com.example.jemcochce.service;

import com.example.jemcochce.model.Skladnik;
import com.example.jemcochce.repository.SkladnikRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkladnikService {
    private final SkladnikRepository skladnikRepository;


    public boolean addSkladnik(Skladnik skladnik){
        skladnikRepository.save(skladnik);
        return true;
    }
}
