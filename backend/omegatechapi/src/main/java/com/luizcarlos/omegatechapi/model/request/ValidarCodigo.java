package com.luizcarlos.omegatechapi.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Dados de validação do código")
@Data
public class ValidarCodigo {

    @Schema(example = "usuario@email.com")
    private String email;

    @Schema(example = "100200")
    private String codigo;
}
