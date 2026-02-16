package com.luizcarlos.omegatechapi.model.request;
import lombok.Data;

@Data

public class CriarTecnicoRequest {
    private String nome;
    private String email;
    private String senhaInicial;
}
