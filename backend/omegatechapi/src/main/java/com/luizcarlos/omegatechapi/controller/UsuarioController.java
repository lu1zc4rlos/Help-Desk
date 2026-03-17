package com.luizcarlos.omegatechapi.controller;

import com.luizcarlos.omegatechapi.model.dto.CadastroUsuarioDTO;
import com.luizcarlos.omegatechapi.model.dto.ErrorResponseDTO;
import com.luizcarlos.omegatechapi.model.dto.LoginRequestDTO;
import com.luizcarlos.omegatechapi.model.entity.Usuario;
import com.luizcarlos.omegatechapi.model.request.AlterarSenhaRequest;
import com.luizcarlos.omegatechapi.model.request.ResetarSenhaComCodigo;
import com.luizcarlos.omegatechapi.model.request.SolicitarCodigoRequest;
import com.luizcarlos.omegatechapi.model.request.ValidarCodigo;
import com.luizcarlos.omegatechapi.model.dto.AuthResponseDTO;
import com.luizcarlos.omegatechapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Operações relacionadas ao gerenciamento de usuários do sistema")
public class UsuarioController {

    @Autowired
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Login do usuário",
            description = "Login dos usuários do sistema"
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login efetuado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        AuthResponseDTO response = usuarioService.loginUsuario(loginRequestDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Cadastro do usuário",
            description = "Cadastro dos novos usuários do sistema"
    )
    @ApiResponses( value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Cadastro efetuado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PostMapping("/cadastro")
    public ResponseEntity<AuthResponseDTO> cadastro(@Valid @RequestBody CadastroUsuarioDTO dto){
        AuthResponseDTO response = usuarioService.cadastrarNovoUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Alteração de senha",
            description = "Alteração de senha via senha atual"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Alteração de senha efetuada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PutMapping("alterar_senha")
    public ResponseEntity<Void> alterarSenha(@RequestBody AlterarSenhaRequest request) {
        usuarioService.alterarSenha(request);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Solicitar código",
            description = "Solicitação de código via email para troca de senha"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Código enviado com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PostMapping("/solicitar_codigo")
    public ResponseEntity<Void> solicitarCodigo(@RequestBody SolicitarCodigoRequest request) {
        usuarioService.solicitarCodigoRecuperacao(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Validação do código",
            description = "Validação do código que foi enviado para o email"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Código validado com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PostMapping("/validar_codigo")
    public ResponseEntity<Void> validarCodigo(@RequestBody ValidarCodigo dto) {
        usuarioService.validarCodigoRecuperacao(dto);
        return ResponseEntity.ok().build();
    }


    @Operation(
            summary = "Troca de senha via código",
            description = "Troca de senha via código enviado por email"
    )
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
    })
    @PutMapping("/resetar_senha")
    public ResponseEntity<Void> resetarSenha(@RequestBody ResetarSenhaComCodigo dto) {
        usuarioService.resetarSenhaComCodigo(dto);
        return ResponseEntity.ok().build();
    }
}
