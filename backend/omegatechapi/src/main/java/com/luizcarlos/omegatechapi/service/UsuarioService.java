
package com.luizcarlos.omegatechapi.service;

import com.luizcarlos.omegatechapi.config.exception.*;
import com.luizcarlos.omegatechapi.model.dto.CadastroUsuarioDTO;
import com.luizcarlos.omegatechapi.model.entity.TokenRecuperacao;
import com.luizcarlos.omegatechapi.model.entity.Usuario;
import com.luizcarlos.omegatechapi.model.enums.Perfil;
import com.luizcarlos.omegatechapi.model.request.AlterarSenhaRequest;
import com.luizcarlos.omegatechapi.model.request.ResetarSenhaComCodigo;
import com.luizcarlos.omegatechapi.model.request.ValidarCodigo;
import com.luizcarlos.omegatechapi.repository.TokenRecuperacaoRepository;
import com.luizcarlos.omegatechapi.repository.UsuarioRepository;
import com.luizcarlos.omegatechapi.model.dto.AuthResponseDTO;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class UsuarioService {

    @Autowired
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TokenRecuperacaoRepository tokenRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }
    public AuthResponseDTO loginUsuario(Usuario request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Credenciais inválidas"));

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new UnauthorizedException("Credenciais inválidas");
        }

        if(request.getEmail() == null || request.getSenha() == null){
            throw new BadRequestException("Email e senha são obrigatórios");
        }

        String token = jwtService.gerarToken(usuario);

        return new AuthResponseDTO(usuario.getNome(), token, usuario.getPerfil().toString());
    }
    public AuthResponseDTO cadastrarNovoUsuario(CadastroUsuarioDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("Este e-mail já está em uso!");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dto.getNome());
        novoUsuario.setEmail(dto.getEmail());
        novoUsuario.setDataNascimento(dto.getDataNascimento());
        novoUsuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        novoUsuario.setPerfil(Perfil.ROLE_CLIENTE);

        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        String token = jwtService.gerarToken(novoUsuario);


        try {
            emailService.enviarEmailDeBoasVindas(usuarioSalvo.getEmail(), usuarioSalvo.getNome());
        } catch (Exception e) {
            log.error("Falha ao enviar e-mail de boas-vindas", e);
        }

        return new AuthResponseDTO(usuarioSalvo.getNome(), token, novoUsuario.getPerfil().toString());
    }
    public void alterarSenha(AlterarSenhaRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o email: " + request.getEmail()));

        if (!passwordEncoder.matches(request.getSenhaAtual(), usuario.getSenha())) {
            throw new UnauthorizedException("Credenciais inválidas");
        }

        String novaSenhaCodificada = passwordEncoder.encode(request.getNovaSenha());
        usuario.setSenha(novaSenhaCodificada);

        usuarioRepository.save(usuario);

        try {
            emailService.enviarEmailTrocaDeSenha(usuario.getEmail());
        } catch (Exception e) {
            log.error("Falha ao enviar e-mail de alteração da senha", e);
        }
    }

    @Transactional
    public void solicitarCodigoRecuperacao(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email não encontrado"));

        SecureRandom random = new SecureRandom();
        String codigo = String.format("%06d", random.nextInt(1_000_000));
        LocalDateTime expiracao = LocalDateTime.now().plusMinutes(10);

        Optional<TokenRecuperacao> tokenExistenteOpt = tokenRepository.findByUsuarioId(usuario.getId());

        TokenRecuperacao tokenParaSalvar;

        if (tokenExistenteOpt.isPresent()) {
            tokenParaSalvar = tokenExistenteOpt.get();
            tokenParaSalvar.setCodigo(codigo);
            tokenParaSalvar.setDataExpiracao(expiracao);
            tokenParaSalvar.setValidado(false);
        } else {
            tokenParaSalvar = new TokenRecuperacao(codigo, expiracao, usuario);
        }

        tokenRepository.save(tokenParaSalvar);

        try {
            emailService.enviarEmailCodigo(email, codigo);
        }
        catch (Exception e) {
            log.error("Falha ao enviar e-mail com código", e);
        }
    }

    @Transactional
    public void validarCodigoRecuperacao(ValidarCodigo dto) {

        TokenRecuperacao token = tokenRepository.findByCodigo(dto.getCodigo())
                .orElseThrow(() -> new ResourceNotFoundException("Código inválido."));

        if (!token.getUsuario().getEmail().equals(dto.getEmail())) {
            throw new UnauthorizedException("Código inválido para este email.");
        }

        if (token.getDataExpiracao().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(token);
            throw new UnauthorizedException("Código expirado. Peça um novo.");
        }

        if (token.isValidado()) {
            throw new ConflictException("Código já utilizado.");
        }

        token.setValidado(true);
        token.setDataExpiracao(LocalDateTime.now().plusMinutes(5));

        tokenRepository.save(token);
    }
    @Transactional
    public void resetarSenhaComCodigo(ResetarSenhaComCodigo dto) {

        log.debug("Tentativa de reset de senha para email {}", dto.getEmail());

        TokenRecuperacao token = tokenRepository.findByCodigo(dto.getCodigo())
                .orElseThrow(() -> new ResourceNotFoundException("Código não encontrado"));

        if (!token.getUsuario().getEmail().equalsIgnoreCase(dto.getEmail().trim())) {
            throw new UnauthorizedException("Código ou Email inválidos.");
        }

        if (!token.isValidado()) {
            throw new UnauthorizedException("Código não foi validado previamente.");
        }

        if (token.getDataExpiracao().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(token);
            throw new UnauthorizedException("Sessão para troca de senha expirou. Peça um novo código.");
        }

        Usuario usuario = token.getUsuario();

        usuario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
        usuarioRepository.save(usuario);

        tokenRepository.delete(token);

        try {
            emailService.enviarEmailTrocaDeSenha(usuario.getEmail());
        } catch (Exception e) {
            log.error("Erro ao agendar envio de e-mail", e);
        }
    }
}