package com.luizcarlos.omegatechapi.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Dado enviado ao chatbot")
@Data
public class MensagemRequest {

    @Schema(example = "estou com um problema de helpdesk")
    private String mensagem;
}
