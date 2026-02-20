package com.luizcarlos.omegatechapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;
    private LocalDate dataCriacao;
    private String prioridade;
    private String status;
    private String resposta;

    private Long clienteId;
    private String nomeCliente;

    private Long tecnicoId;
    private String nomeTecnico;
}
