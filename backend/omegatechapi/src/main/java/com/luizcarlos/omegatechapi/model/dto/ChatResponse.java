package com.luizcarlos.omegatechapi.model.dto;

import com.luizcarlos.omegatechapi.model.entity.Ticket;
import com.luizcarlos.omegatechapi.model.enums.TipoResposta;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ChatResponse {
    private String Resposta;
    private TipoResposta Tipo;
    private List<TicketResponseDTO> tickets;
    private LocalDate Timestamp;
}
