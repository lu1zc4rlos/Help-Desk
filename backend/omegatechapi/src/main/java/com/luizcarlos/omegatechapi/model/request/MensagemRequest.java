package com.luizcarlos.omegatechapi.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Dado enviado ao chatbot")
@Data
public class MensagemRequest {

    @Schema(example = "usuario@email.com")
    private String mensagem;
}
