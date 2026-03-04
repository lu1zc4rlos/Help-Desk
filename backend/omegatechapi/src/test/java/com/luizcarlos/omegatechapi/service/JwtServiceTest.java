package com.luizcarlos.omegatechapi.service;

import com.luizcarlos.omegatechapi.model.entity.Usuario;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setup() {
        String chaveBase64 = Base64.getEncoder()
                .encodeToString("minhachavesecreta123456789123456789".getBytes());

        jwtService = new JwtService(chaveBase64, 60000);
    }

    @Test
    void deveGerarTokenValido(){
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        String token = jwtService.gerarToken(usuario);
        String idExtraido = jwtService.extractUsername(token);
        assertNotNull("1",token);
    }

    @Test
    void deveRetornarTrueQuandoTokenForValido(){
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        String token = jwtService.gerarToken(usuario);
        Boolean valido = jwtService.isTokenValid(token,usuario);

        assertTrue(valido);
    }

    @Test
    void deveRetornarFalseQuandoTokenNaoPertencerAoUsuario(){
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);

        String token = jwtService.gerarToken(usuario1);
        Boolean valido = jwtService.isTokenValid(token,usuario2);

        assertFalse(valido);
    }

    public boolean tokenValid(String token, Usuario usuario) {
        try {
            final String username = jwtService.extractUsername(token);
            return (username.equals(usuario.getUsername()) && !jwtService.isTokenExpired(token));
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    @Test
    void deveRetornarFalseQuandoTokenEstiverExpirado() throws InterruptedException{
        jwtService.setExpirationMs(1);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        String token = jwtService.gerarToken(usuario);
        Thread.sleep(5);

        Boolean Valid = tokenValid(token,usuario);

        assertFalse(Valid);
    }

    @Test
    void deveLancarExcecaoQuandoTokenForInvalido(){
        String tokenInvalido = "token.falso.aqui";

        assertThrows(Exception.class, () -> {
            jwtService.extractUsername(tokenInvalido);
        });
    }
}
