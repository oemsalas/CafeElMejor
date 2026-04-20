package com.cafe.cafeDemo.repository;

import com.cafe.cafeDemo.model.Cobranza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CobranzaRepository extends JpaRepository<Cobranza, Long> {
}
