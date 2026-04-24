package com.cafe.cafeDemo.repository;

import com.cafe.cafeDemo.model.Factura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    @Query("SELECT f FROM Factura f WHERE " +
           "LOWER(f.estado) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(f.cliente.nombre) LIKE LOWER(CONCAT('%',:q,'%'))")
    Page<Factura> search(@Param("q") String q, Pageable pageable);
}
