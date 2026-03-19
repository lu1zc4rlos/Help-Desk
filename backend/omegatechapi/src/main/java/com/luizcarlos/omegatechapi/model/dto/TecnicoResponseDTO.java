package com.luizcarlos.omegatechapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Dados dos técnicos")
@Data
public class TecnicoResponseDTO {

    @Schema(example = "1L")
    private Long id;

    @Schema(example = "técnico")
    private String nome;

    @Schema(example = "AXL123")
    private String matricula;

    @Schema(example = "tecnico@email.com")
    private String email;

    @Schema(example = "2023-03-19")
    private LocalDateTime dataCriacao;
}
