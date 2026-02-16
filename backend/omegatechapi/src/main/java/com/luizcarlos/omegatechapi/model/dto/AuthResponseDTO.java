package com.luizcarlos.omegatechapi.model.dto;

import lombok.Data;

@Data

public class AuthResponseDTO {

    private String token;
    private String username;
    private String perfil;

    public AuthResponseDTO(String username, String token, String perfil) {
        this.username = username;
        this.token = token;
        this.perfil = perfil;
    }
}
