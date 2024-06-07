package com.cafe.cafeDemo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cafe.cafeDemo.model.Producto;

@Repository
public interface ProductosRepository extends JpaRepository<Producto, Long> {
}