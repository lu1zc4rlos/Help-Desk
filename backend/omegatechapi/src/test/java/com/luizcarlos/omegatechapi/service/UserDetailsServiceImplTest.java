package com.luizcarlos.omegatechapi.service;

import com.luizcarlos.omegatechapi.config.exception.ResourceNotFoundException;
import com.luizcarlos.omegatechapi.model.entity.Usuario;
import com.luizcarlos.omegatechapi.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    void deveCarregarUsuarioQuandoIdForValido(){

        Long userId = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(userId);

        when(usuarioRepository.findByUserId(1L))
                .thenReturn(Optional.of(usuario));

        var userDetails = userDetailsServiceImpl.loadUserByUsername("1");

        assertNotNull(userDetails);
        verify(usuarioRepository).findByUserId(1L);
        verify(usuarioRepository, never()).findByEmail(any());

    }

    @Test
    void deveCarregarUsuarioQuandoEmailForValido(){

        String email = "teste@email.com";

        Usuario usuario = new Usuario();
        usuario.setEmail(email);

        when(usuarioRepository.findByEmail(email))
                .thenReturn(Optional.of(usuario));

        var userDetails = userDetailsServiceImpl.loadUserByUsername(email);

        assertNotNull(userDetails);
        verify(usuarioRepository).findByEmail(email);
        verify(usuarioRepository, never()).findByUserId(any());

    }

    @Test
    void deveLancarExcecaoQuandoIdNaoExistir(){

        when(usuarioRepository.findByUserId(99L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()->
                userDetailsServiceImpl.loadUserByUsername("99")
        );

    }

    @Test
    void deveLancarExcecaoQuandoEmailNaoExistir(){

        when(usuarioRepository.findByEmail("naoexiste@email.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()->
                userDetailsServiceImpl.loadUserByUsername("naoexiste@email.com")
        );

    }


}
