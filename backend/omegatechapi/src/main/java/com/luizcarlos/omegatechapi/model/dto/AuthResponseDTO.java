package com.luizcarlos.omegatechapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Response login")
public class AuthResponseDTO {

    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c3VhcmlvQGdtYWlsLmNvbSIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzEwMDAwMDAwLCJleHAiOjE3MTAwMzYwMDB9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
    private String token;
    @Schema(example = "Usuário")
    private String username;
    @Schema(example = "ROLE_CLIENTE")
    private String perfil;

    public AuthResponseDTO(String username, String token, String perfil) {
        this.username = username;
        this.token = token;
        this.perfil = perfil;
    }
}
