package com.cafe.cafeDemo.repository;

import com.cafe.cafeDemo.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE u.usuario = :usuario AND u.password = :password")
    Usuario getUsuarioByPassword(@Param("usuario") String usuario, @Param("password") String password);

    @Query("SELECT u FROM Usuario u WHERE " +
           "LOWER(u.nombreApellido) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           "LOWER(u.usuario) LIKE LOWER(CONCAT('%',:q,'%'))")
    Page<Usuario> search(@Param("q") String q, Pageable pageable);
}
