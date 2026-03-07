package com.luizcarlos.omegatechapi.service;

import com.luizcarlos.omegatechapi.config.exception.BusinessRuleException;
import com.luizcarlos.omegatechapi.config.exception.ConflictException;
import com.luizcarlos.omegatechapi.config.exception.ResourceNotFoundException;
import com.luizcarlos.omegatechapi.model.dto.TecnicoResponseDTO;
import com.luizcarlos.omegatechapi.model.entity.TecnicoProfile;
import com.luizcarlos.omegatechapi.model.entity.Usuario;
import com.luizcarlos.omegatechapi.model.enums.Perfil;
import com.luizcarlos.omegatechapi.repository.TecnicoRepository;
import com.luizcarlos.omegatechapi.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TecnicoServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TecnicoRepository tecnicoRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private TecnicoService tecnicoService;

    @Test
    void deveCriarTecnicoComSucesso(){
        Usuario usuarioEntrada = new Usuario();
        usuarioEntrada.setId(1L);
        usuarioEntrada.setNome("Carlos");
        usuarioEntrada.setEmail("Carlos@gmail.com");
        usuarioEntrada.setSenha("123456");
        usuarioEntrada.setDataNascimento(LocalDate.of(200,1,1));

        when(passwordEncoder.encode("123456")).thenReturn("senhaCriptografada");

        when(tecnicoRepository.existsByMatricula(anyString())).thenReturn(false);

        when(usuarioRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

            tecnicoService.criarTecnico(usuarioEntrada);

            verify(tecnicoRepository).save(any(TecnicoProfile.class));
            verify(passwordEncoder).encode("123456");

            verify(usuarioRepository).save(argThat(usuario ->
                    usuario.getPerfil() == Perfil.ROLE_TECNICO
            ));
    }

    @Test
    void deveLancarConflictExceptionQuandoTecnicoJaExiste(){
        Usuario usuarioEntrada = new Usuario();
        usuarioEntrada.setId(1L);
        usuarioEntrada.setNome("Carlos");
        usuarioEntrada.setEmail("Carlos@gmail.com");
        usuarioEntrada.setSenha("123456");
        usuarioEntrada.setDataNascimento(LocalDate.of(200,1,1));

        when(passwordEncoder.encode("123456")).thenReturn("senhaCriptografada");

        when(tecnicoRepository.existsByMatricula(anyString())).thenReturn(false);

        when(usuarioRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(tecnicoRepository.save(any())).thenThrow(new DataIntegrityViolationException("Erro de integridade"));
        assertThrows(ConflictException.class, ()-> {
            tecnicoService.criarTecnico(usuarioEntrada);
        });
    }

    @Test
    void deveRetornarListaDeTecnicos(){
        Usuario usuarioEntrada = new Usuario();
        usuarioEntrada.setId(1L);
        usuarioEntrada.setNome("Carlos");
        usuarioEntrada.setEmail("Carlos@gmail.com");
        usuarioEntrada.setPerfil(Perfil.ROLE_TECNICO);

        TecnicoProfile tecnicoProfile = new TecnicoProfile();
        tecnicoProfile.setMatricula("TEC123");
        usuarioEntrada.setTecnicoProfile(tecnicoProfile);

        when(usuarioRepository.findAllByPerfil(Perfil.ROLE_TECNICO)).thenReturn(List.of(usuarioEntrada));

        List<TecnicoResponseDTO> list = tecnicoService.buscarTodosTecnicos();

        assertEquals(1,list.size());
        assertEquals("Carlos", list.get(0).getNome());
        assertEquals("TEC123", list.get(0).getMatricula());

    }

    @Test
    void deveRetornarMatriculaNAQuandoTecnicoNaoPossuiProfile(){
        Usuario usuarioEntrada = new Usuario();
        usuarioEntrada.setId(1L);
        usuarioEntrada.setNome("Carlos");
        usuarioEntrada.setEmail("Carlos@gmail.com");
        usuarioEntrada.setPerfil(Perfil.ROLE_TECNICO);
        usuarioEntrada.setTecnicoProfile(null);

        when(usuarioRepository.findAllByPerfil(Perfil.ROLE_TECNICO)).thenReturn(List.of(usuarioEntrada));

        List<TecnicoResponseDTO> list = tecnicoService.buscarTodosTecnicos();

        assertEquals(1,list.size());
        assertEquals("N/A", list.get(0).getMatricula());
    }

    @Test
    void devebuscarTecnicoPorId(){
        Usuario usuarioEntrada = new Usuario();
        usuarioEntrada.setId(1L);
        usuarioEntrada.setNome("Carlos");
        usuarioEntrada.setEmail("Carlos@gmail.com");
        usuarioEntrada.setPerfil(Perfil.ROLE_TECNICO);

        TecnicoProfile tecnicoProfile = new TecnicoProfile();
        tecnicoProfile.setMatricula("TEC123");
        usuarioEntrada.setTecnicoProfile(tecnicoProfile);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntrada));

        TecnicoResponseDTO tecnicoResponseDTO = tecnicoService.buscarTecnicoPorId(1L);

        assertNotNull(tecnicoResponseDTO);
        assertEquals("Carlos",tecnicoResponseDTO.getNome());

       verify(usuarioRepository).findById(1L);
    }

    @Test
    void deveLancarResourceNotFoundExceptionQuandoTecnicoNaoExiste(){

        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()-> {
            tecnicoService.buscarTecnicoPorId(1L);
                });
        verify(usuarioRepository).findById(1L);
    }

    @Test
    void deveLancarBusinessRuleExceptionQuandoUsuarioNaoForTecnico(){
        Usuario usuarioEntrada = new Usuario();
        usuarioEntrada.setId(1L);
        usuarioEntrada.setPerfil(Perfil.ROLE_CLIENTE);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEntrada));

        assertThrows(BusinessRuleException.class, ()-> {
            tecnicoService.buscarTecnicoPorId(1L);
        });
    }

}
