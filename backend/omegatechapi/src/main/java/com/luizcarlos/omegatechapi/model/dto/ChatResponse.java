package com.luizcarlos.omegatechapi.model.dto;

import com.luizcarlos.omegatechapi.model.enums.TipoResposta;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ChatResponse {
    private String Resposta;
    private TipoResposta Tipo;
    private Object Dados;
    private LocalDate Timestamp;
}
