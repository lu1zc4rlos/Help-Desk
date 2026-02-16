package com.luizcarlos.omegatechapi.repository;

import com.luizcarlos.omegatechapi.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t FROM Ticket t WHERE t.cliente.id = :usuarioId ORDER BY t.dataCriacao DESC")
    Ticket findUltimoChamadoPorUsuario(@Param("usuarioId") Long usuarioId);

    // 🔹 Todos os chamados do cliente
    @Query("SELECT t FROM Ticket t WHERE t.cliente.id = :usuarioId ORDER BY t.dataCriacao DESC")
    List<Ticket> findChamadosPorUsuario(@Param("usuarioId") Long usuarioId);

    // 🔹 Chamados recentes (pode ser os 5 últimos, por exemplo, limitado via código)
    @Query("SELECT t FROM Ticket t WHERE t.cliente.id = :usuarioId ORDER BY t.dataCriacao DESC")
    List<Ticket> findChamadosRecentes(@Param("usuarioId") Long usuarioId);
}
