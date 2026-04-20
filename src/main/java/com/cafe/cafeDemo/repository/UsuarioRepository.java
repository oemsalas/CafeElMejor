package com.cafe.cafeDemo.repository;

import com.cafe.cafeDemo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE u.usuario = :usuario AND u.password = :password")
    Usuario getUsuarioByPassword(@Param("usuario") String usuario, @Param("password") String password);
}
