package com.luizcarlos.omegatechapi.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Dados da Troca de senha por senha atual")
@Data
public class AlterarSenhaRequest {

    @Schema(example = "usuario@email.com")
    private String email;

    @Schema(example = "123456")
    private String senhaAtual;

    @Schema(example = "654321")
    private String novaSenha;

}
