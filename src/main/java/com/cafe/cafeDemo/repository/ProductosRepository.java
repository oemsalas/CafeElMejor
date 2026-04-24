package com.cafe.cafeDemo.repository;

import com.cafe.cafeDemo.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductosRepository extends JpaRepository<Producto, Long> {

    @Query("SELECT p FROM Producto p WHERE " +
           "LOWER(p.descripcion) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(p.lote) LIKE LOWER(CONCAT('%',:q,'%'))")
    Page<Producto> search(@Param("q") String q, Pageable pageable);
}
