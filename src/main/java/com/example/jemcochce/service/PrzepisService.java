package com.example.jemcochce.service;

import com.example.jemcochce.model.Account;
import com.example.jemcochce.model.IloscSkladnika;
import com.example.jemcochce.model.Przepis;
import com.example.jemcochce.model.Skladnik;
import com.example.jemcochce.repository.IloscSkladnikaRepository;
import com.example.jemcochce.repository.PrzepisRepository;
import com.example.jemcochce.repository.SkladnikRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrzepisService {
    private final SkladnikRepository skladnikRepository;
    private final IloscSkladnikaRepository iloscSkladnikaRepository;
    private final PrzepisRepository przepisRepository;

    public void addPrzepis(String przepisName, Account account) {

        przepisRepository.save(Przepis.builder()
                .name(przepisName)
                .creator(account)
                .build());
    }

    public void addSkladnik(String skladnikName) {

        skladnikRepository.save(Skladnik.builder().nazwa(skladnikName).build());
    }

    public List<Przepis> getAllPrzepisy() {
        return przepisRepository.findAll();
    }

    public List<Skladnik> getAllSkladniki() {
        return skladnikRepository.findAll();
    }

    public void addSkladnikToPrzepis(Long przepis_id, Long skladnik_id, double ilosc) {
        Optional<Skladnik> skladnikOptional = skladnikRepository.findById(skladnik_id);
        Optional<Przepis> przepisOptional = przepisRepository.findById(przepis_id);
        if (skladnikOptional.isPresent() && przepisOptional.isPresent()) {
            IloscSkladnika iloscSkladnika = IloscSkladnika.builder()
                    .przepis(przepisOptional.get())
                    .skladnik(skladnikOptional.get())
                    .ilosc(ilosc)
                    .build();

            iloscSkladnikaRepository.save(iloscSkladnika);
        }
    }
}
