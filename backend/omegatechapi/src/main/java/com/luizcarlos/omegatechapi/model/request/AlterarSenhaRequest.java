package com.luizcarlos.omegatechapi.model.request;

import lombok.Data;

@Data
public class AlterarSenhaRequest {
    private String email;
    private String senhaAtual;
    private String novaSenha;

}
