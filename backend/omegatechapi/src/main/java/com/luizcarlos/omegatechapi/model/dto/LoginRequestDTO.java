package com.luizcarlos.omegatechapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Dados de login")
public class LoginRequestDTO {

    @Schema(example = "usuario@gmail.com")
    private String email;

    @Schema(example = "123456")
    private String senha;
}
