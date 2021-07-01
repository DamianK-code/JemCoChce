package com.example.jemcochce.repository;

import com.example.jemcochce.model.Skladnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkladnikRepository extends JpaRepository<Skladnik, Long> {
}
