package com.cafe.cafeDemo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cafe.cafeDemo.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	@Query(value="select * from usuario a where a.usuario= :usuario and a.password=:password", nativeQuery=true)
	Usuario getUsuarioByPassword(String usuario, String password);
}