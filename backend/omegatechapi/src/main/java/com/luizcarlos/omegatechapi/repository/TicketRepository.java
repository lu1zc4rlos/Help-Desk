package com.luizcarlos.omegatechapi.repository;

import com.luizcarlos.omegatechapi.model.enums.Status;
import com.luizcarlos.omegatechapi.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    <S extends Ticket> S save(S entity);

    List<Ticket> findByClienteId(Long clienteId);
    List<Ticket> findByClienteIdAndStatus(Long clienteId, Status status);
    List<Ticket> findByStatus(Status status);
    @Query("SELECT t FROM Ticket t WHERE t.status = :status AND t.tecnicoAtribuido.id = :tecnicoId")
    List<Ticket> findByStatusAndTecnicoResponsavel(
            @Param("status") Status status,
            @Param("tecnicoId") Long tecnicoId);
}
