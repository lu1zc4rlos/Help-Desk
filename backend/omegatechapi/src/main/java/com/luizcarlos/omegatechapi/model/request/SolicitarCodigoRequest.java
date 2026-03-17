package com.luizcarlos.omegatechapi.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Email para solicitação de código")
@Data
public class SolicitarCodigoRequest {

    @Schema(example = "usuario@email.com")
    private String email;
}
