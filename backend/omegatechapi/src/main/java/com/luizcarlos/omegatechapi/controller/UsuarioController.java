package com.luizcarlos.omegatechapi.controller;

import com.luizcarlos.omegatechapi.model.dto.CadastroUsuarioDTO;
import com.luizcarlos.omegatechapi.model.entity.Usuario;
import com.luizcarlos.omegatechapi.model.request.AlterarSenhaRequest;
import com.luizcarlos.omegatechapi.model.request.ResetarSenhaComCodigo;
import com.luizcarlos.omegatechapi.model.request.SolicitarCodigoRequest;
import com.luizcarlos.omegatechapi.model.request.ValidarCodigo;
import com.luizcarlos.omegatechapi.model.dto.AuthResponseDTO;
import com.luizcarlos.omegatechapi.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")

public class UsuarioController {

    @Autowired
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody Usuario usuario){
        AuthResponseDTO response = usuarioService.loginUsuario(usuario);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<AuthResponseDTO> cadastro(@Valid @RequestBody CadastroUsuarioDTO dto){
        AuthResponseDTO response = usuarioService.cadastrarNovoUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("alterar_senha")
    public ResponseEntity<Void> alterarSenha(@RequestBody AlterarSenhaRequest request) {
        usuarioService.alterarSenha(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/solicitar_codigo")
    public ResponseEntity<Void> solicitarCodigo(@RequestBody SolicitarCodigoRequest request) {
        usuarioService.solicitarCodigoRecuperacao(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/validar_codigo")
    public ResponseEntity<Void> validarCodigo(@RequestBody ValidarCodigo dto) {
        usuarioService.validarCodigoRecuperacao(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/resetar_senha")
    public ResponseEntity<Void> resetarSenha(@RequestBody ResetarSenhaComCodigo dto) {
        usuarioService.resetarSenhaComCodigo(dto);
        return ResponseEntity.ok().build();
    }
}
