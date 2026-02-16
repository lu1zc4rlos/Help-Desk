package com.luizcarlos.omegatechapi.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CadastroUsuarioDTO {
    @NotBlank
    private String nome;

    @Email
    @NotBlank
    private String email;

    @NotNull
    private LocalDate dataNascimento;

    @NotBlank
    private String senha;
}
