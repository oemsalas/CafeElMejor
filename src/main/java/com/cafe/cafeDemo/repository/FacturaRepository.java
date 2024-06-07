package com.cafe.cafeDemo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cafe.cafeDemo.model.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
}