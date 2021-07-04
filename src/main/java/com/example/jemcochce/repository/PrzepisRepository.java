package com.example.jemcochce.repository;

import com.example.jemcochce.model.Przepis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrzepisRepository extends JpaRepository<Przepis, Long> {
}
