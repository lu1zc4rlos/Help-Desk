package com.luizcarlos.omegatechapi.repository;

import com.luizcarlos.omegatechapi.model.entity.TokenRecuperacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRecuperacaoRepository extends JpaRepository<TokenRecuperacao, Long> {
    Optional<TokenRecuperacao> findByCodigo(String codigo);
    Optional<TokenRecuperacao> findByUsuarioId(Long usuarioId);

}
