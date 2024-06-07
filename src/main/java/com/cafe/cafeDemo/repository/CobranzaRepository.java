package com.cafe.cafeDemo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cafe.cafeDemo.model.Cobranza;

@Repository
public interface CobranzaRepository extends JpaRepository<Cobranza, Long> {
}