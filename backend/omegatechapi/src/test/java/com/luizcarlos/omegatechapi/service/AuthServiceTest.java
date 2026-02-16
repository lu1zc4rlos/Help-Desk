package com.luizcarlos.omegatechapi.service;

import com.luizcarlos.omegatechapi.config.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    Principal principal = mock(Principal.class);

    @Test
    void  deveRetornarUserIdQuandoPrincipalValido(){

        when( principal.getName()).thenReturn("1");

        Long userId = AuthService.extractUserId(principal);

        assertEquals(1L, userId);
    }

    @Test
    void deveLancarExcecaoQuandoPrincipalForNull(){
        assertThrows(UnauthorizedException.class, ()->
                AuthService.extractUserId(null)
        );
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNull(){

        when( principal.getName()).thenReturn(null);

        assertThrows(UnauthorizedException.class, ()->
                AuthService.extractUserId(null)
        );
    }

    @Test
    void deveLancarExcecaoQuandoNomeNaoForNumero(){

        when( principal.getName()).thenReturn("abc");

        assertThrows(NumberFormatException.class, ()->
                AuthService.extractUserId(principal)
        );
    }
}
