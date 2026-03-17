package com.luizcarlos.omegatechapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "DTO de cadastro do usuário")
@Data
public class CadastroUsuarioDTO {

    @Schema(example = "Usuário")
    @NotBlank
    private String nome;

    @Schema(example = "usuario@email.com")
    @Email
    @NotBlank
    private String email;

    @Schema(example = "2001-01-01")
    @NotNull
    private LocalDate dataNascimento;

    @Schema(example = "123456")
    @NotBlank
    private String senha;
}
