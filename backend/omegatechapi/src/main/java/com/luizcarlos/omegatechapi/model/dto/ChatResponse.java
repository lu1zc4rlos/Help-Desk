package com.luizcarlos.omegatechapi.model.dto;

import com.luizcarlos.omegatechapi.model.entity.Ticket;
import com.luizcarlos.omegatechapi.model.enums.TipoResposta;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Dados de retorno do chat ")
@Data
public class ChatResponse {

    @Schema(example = "Olá! Como posso ajudar você com questões técnicas hoje?")
    private String resposta;

    @Schema(example = "GPT")
    private TipoResposta tipo;

    @Schema(example = "null")
    private List<TicketResponseDTO> tickets;

    @Schema(example = "2026-03-17")
    private LocalDate timestamp;
}
