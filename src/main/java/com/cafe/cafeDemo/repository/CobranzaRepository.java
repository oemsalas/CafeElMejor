package com.cafe.cafeDemo.repository;

import com.cafe.cafeDemo.model.Cobranza;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CobranzaRepository extends JpaRepository<Cobranza, Long> {

    @Query("SELECT c FROM Cobranza c WHERE " +
           "LOWER(c.metodoDePago) LIKE LOWER(CONCAT('%',:q,'%'))")
    Page<Cobranza> search(@Param("q") String q, Pageable pageable);
}
