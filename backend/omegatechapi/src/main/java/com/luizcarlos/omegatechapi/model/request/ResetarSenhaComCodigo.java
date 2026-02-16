package com.luizcarlos.omegatechapi.model.request;

import lombok.Data;

@Data
public class ResetarSenhaComCodigo {
    private String email;
    private String codigo;
    private String novaSenha;
}
