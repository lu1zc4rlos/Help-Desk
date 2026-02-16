package com.luizcarlos.omegatechapi.repository;

import com.luizcarlos.omegatechapi.model.enums.Perfil;
import com.luizcarlos.omegatechapi.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findById(Long id);

    boolean existsByEmail(String email);
    @Query("SELECT u FROM Usuario u WHERE u.id = :id")
    Optional<Usuario> findByUserId(@Param("id") Long id);

    @Query("SELECT u FROM Usuario u JOIN FETCH u.tecnicoProfile WHERE u.perfil = 'ROLE_TECNICO'")
    List<Usuario> findAllTecnicosWithProfile();

    List<Usuario> findAllByPerfil(Perfil perfil);
}
