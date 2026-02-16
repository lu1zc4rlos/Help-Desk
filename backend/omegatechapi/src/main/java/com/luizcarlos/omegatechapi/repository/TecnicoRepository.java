package com.luizcarlos.omegatechapi.repository;

import com.luizcarlos.omegatechapi.model.entity.TecnicoProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TecnicoRepository extends JpaRepository<TecnicoProfile, Long> {
    boolean existsByMatricula(String matricula);
}
