package com.luizcarlos.omegatechapi.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Dados para alteração de senha via código")
@Data
public class ResetarSenhaComCodigo {

    @Schema(example = "usuario@email.com")
    private String email;

    @Schema(example = "100200")
    private String codigo;

    @Schema(example = "123456")
    private String novaSenha;
}
