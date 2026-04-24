package com.cafe.cafeDemo.repository;

import com.cafe.cafeDemo.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("SELECT c FROM Cliente c WHERE " +
           "LOWER(c.nombre) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "CAST(c.dni AS string) LIKE CONCAT('%',:q,'%')")
    Page<Cliente> search(@Param("q") String q, Pageable pageable);
}
