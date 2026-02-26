package com.luizcarlos.omegatechapi.service;

import com.luizcarlos.omegatechapi.config.exception.BadRequestException;
import com.luizcarlos.omegatechapi.model.dto.ChatIntentDTO;
import com.luizcarlos.omegatechapi.model.dto.ChatResponse;
import com.luizcarlos.omegatechapi.model.dto.TicketResponseDTO;
import com.luizcarlos.omegatechapi.model.entity.Ticket;
import com.luizcarlos.omegatechapi.model.entity.Usuario;
import com.luizcarlos.omegatechapi.model.enums.Prioridade;
import com.luizcarlos.omegatechapi.model.enums.Status;
import com.luizcarlos.omegatechapi.model.enums.TipoProblema;
import com.luizcarlos.omegatechapi.model.enums.TipoResposta;
import com.luizcarlos.omegatechapi.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

    @InjectMocks
    private ChatService chatService;

    @Mock
    private OpenAiService openAiService;

    @Mock
    private TicketRepository ticketRepository;

    @Test
    void deveLancarExcecaoQuandoMensagemForNula(){

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        assertThrows(BadRequestException.class,
                () -> chatService.processarMensagem(null, usuario));
    }


    @Test
    void deveConsultarTodosTicketsQuandoIntencaoForTodos(){

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Usuario tecnico = new Usuario();
        tecnico.setId(2L);

        Ticket ticket = new Ticket();
        ticket.setId(100L);
        ticket.setCliente(usuario);
        ticket.setTecnicoAtribuido(tecnico);

        ChatIntentDTO chatIntentDTO = new ChatIntentDTO();
        chatIntentDTO.setIntent("CONSULTAR_TICKETS");
        chatIntentDTO.setStatus("TODOS");

        when(openAiService.classificarIntencao(any()))
                .thenReturn(chatIntentDTO);

        List<Ticket> tickets = List.of(ticket);
        when(ticketRepository.findByClienteId(1L))
                .thenReturn(tickets);

        when(openAiService.gerarRespostaComContexto(any(), any()))
                .thenReturn("Resposta simulada");

        ChatResponse response = chatService.processarMensagem("Meus chamados", usuario);

        assertEquals(TipoResposta.CONSULTA_BD, response.getTipo());
        verify(ticketRepository).findByClienteId(1L);
    }

    @Test
    void deveConsultarTicketsPorStatus() {

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        Usuario tecnico = new Usuario();
        tecnico.setId(2L);

        Ticket ticket = new Ticket();
        ticket.setId(100L);
        ticket.setCliente(usuario);
        ticket.setTecnicoAtribuido(tecnico);

        ChatIntentDTO intent = new ChatIntentDTO();
        intent.setIntent("CONSULTAR_TICKETS");
        intent.setStatus("CONCLUIDO");

        when(openAiService.classificarIntencao(any()))
                .thenReturn(intent);

        when(ticketRepository.findByClienteIdAndStatus(1L, Status.CONCLUIDO))
                .thenReturn(List.of(ticket));

        when(openAiService.gerarRespostaComContexto(any(), any()))
                .thenReturn("Resposta filtrada");

        ChatResponse response = chatService.processarMensagem("Chamados concluidos", usuario);

        assertEquals(TipoResposta.CONSULTA_BD, response.getTipo());

        verify(ticketRepository).findByClienteIdAndStatus(1L, Status.CONCLUIDO);
        verify(ticketRepository, never()).findByClienteId(any());
    }

    @Test
    void deveEnviarParaOpenAIQuandoNaoForConsulta() {

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        ChatIntentDTO intent = new ChatIntentDTO();
        intent.setIntent("CONVERSA_NORMAL");

        when(openAiService.classificarIntencao(any()))
                .thenReturn(intent);

        when(openAiService.enviarParaOpenAI(any()))
                .thenReturn("Resposta GPT");

        ChatResponse response = chatService.processarMensagem("Oi", usuario);

        assertEquals(TipoResposta.GPT, response.getTipo());

        verify(openAiService).enviarParaOpenAI("Oi");
        verify(ticketRepository, never()).findByClienteId(any());
    }

    @Test
    void deveConverterTicketParaDTO() {

        Ticket ticket = new Ticket();
        ticket.setId(10L);
        ticket.setTitulo(TipoProblema.CONEXAO_INSTAVEL);
        ticket.setDescricao("Problema teste");
        ticket.setDataCriacao(LocalDate.now());
        ticket.setPrioridade(Prioridade.ALTA);
        ticket.setResposta("Resposta teste");
        ticket.setStatus(Status.PENDENTE);
        ticket.setTitulo(TipoProblema.ARQUIVO_CORROMPIDO);

        Usuario cliente = new Usuario();
        cliente.setId(1L);
        cliente.setNome("Luiz");

        Usuario tecnico = new Usuario();
        tecnico.setId(2L);
        tecnico.setNome("Carlos");

        ticket.setCliente(cliente);
        ticket.setTecnicoAtribuido(tecnico);

        TicketResponseDTO dto = chatService.converterParaDTO(ticket);
    }



}
