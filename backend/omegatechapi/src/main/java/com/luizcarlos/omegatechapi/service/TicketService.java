package com.luizcarlos.omegatechapi.service;

import com.luizcarlos.omegatechapi.config.exception.*;
import com.luizcarlos.omegatechapi.model.dto.TicketResponseDTO;
import com.luizcarlos.omegatechapi.model.entity.Ticket;
import com.luizcarlos.omegatechapi.model.entity.Usuario;
import com.luizcarlos.omegatechapi.model.enums.Perfil;
import com.luizcarlos.omegatechapi.model.enums.Prioridade;
import com.luizcarlos.omegatechapi.model.enums.Status;
import com.luizcarlos.omegatechapi.model.enums.TipoProblema;
import com.luizcarlos.omegatechapi.repository.TicketRepository;
import com.luizcarlos.omegatechapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.transaction.Transactional;

import org.springframework.http.HttpHeaders;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    public void criarTicket(String mensagem, Usuario usuario) {

        if(mensagem == null || mensagem.isBlank()){
            throw new BadRequestException("Mensagem do ticket é obrigatória");
        }
        if(usuario == null){
            throw new ResourceNotFoundException("Usuário autenticado não encontrado");
        }

        TicketClassificado detalhesIA;
        try {
            detalhesIA = classificarTicketComIA(mensagem);

        } catch (Exception e) {
            throw new ExternalServiceException("Erro ao classificar ticket com IA");
        }
        Ticket novoTicket = new Ticket();
        novoTicket.setTitulo(detalhesIA.titulo);
        novoTicket.setPrioridade(detalhesIA.prioridade);
        novoTicket.setDescricao(mensagem);
        novoTicket.setCliente(usuario);
        novoTicket.setDataCriacao(LocalDate.now());
        novoTicket.setStatus(Status.PENDENTE);

        ticketRepository.save(novoTicket);

    }

    private TicketClassificado classificarTicketComIA(String mensagem) {
        try {
            String systemPrompt = "Você é um classificador de tickets de TI. Analise a mensagem do usuário."
                    + " Você DEVE responder APENAS com duas palavras separadas por vírgula."
                    + " A primeira palavra deve ser um 'TipoProblema' da lista: " + Arrays.toString(TipoProblema.values())
                    + " A segunda palavra deve ser uma 'Prioridade' da lista: " + Arrays.toString(Prioridade.values())
                    + " Exemplo de resposta: COMPUTADOR_TRAVANDO,ALTA";

            String respostaIA = enviarParaOpenAI(systemPrompt, mensagem);

            String[] partes = respostaIA.trim().split(",");

            if (partes.length == 2) {
                TipoProblema titulo = TipoProblema.valueOf(partes[0].trim());
                Prioridade prioridade = Prioridade.valueOf(partes[1].trim());
                return new TicketClassificado(titulo, prioridade);
            }
        } catch (Exception e) {
            throw new ExternalServiceException("Erro ao classificar com IA");
        }

        return new TicketClassificado(TipoProblema.SOLICITACAO_DE_ATENDIMENTO_REMOTO, Prioridade.MEDIA);
    }

    private static class TicketClassificado {
        TipoProblema titulo;
        Prioridade prioridade;

        TicketClassificado(TipoProblema titulo, Prioridade prioridade) {
            this.titulo = titulo;
            this.prioridade = prioridade;
        }
    }

    private String enviarParaOpenAI(String systemPrompt, String userPrompt) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o-mini"); // Modelo rápido e barato
        body.put("temperature", 0.0); // 0.0 para respostas diretas
        body.put("messages", List.of(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userPrompt)
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    OPENAI_URL,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            if (choices == null && choices.isEmpty()) {
                throw new ExternalServiceException("Resposta inválida da OpenAI");
            }
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

            return message.get("content").toString().trim();

        } catch (Exception e) {
            throw new ExternalServiceException("Falha na comunicação com a OpenAI");
        }
    }

    public List<TicketResponseDTO> findTicketsByUserIdAndStatus(Long userId, String statusFilter) {

        if(userId == null){
            throw new UnauthorizedException("Usuário não autenticado");
        }
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        boolean isTecnico = usuario.getPerfil().equals(Perfil.ROLE_TECNICO);

        List<Ticket> tickets;
        String cleanedStatus = (statusFilter != null) ? statusFilter.trim().toUpperCase() : null;

        if (cleanedStatus == null || cleanedStatus.isEmpty() || cleanedStatus.equals("TODOS")) {

            if (isTecnico) {
                tickets = ticketRepository.findAll();
            } else {
                tickets = ticketRepository.findByClienteId(userId);
            }

        } else {
            try {
                Status statusEnum = convertToStatus(cleanedStatus);

                if (isTecnico) {
                    tickets = ticketRepository.findByStatus(statusEnum);
                } else {
                    tickets = ticketRepository.findByClienteIdAndStatus(userId, statusEnum);
                }

            } catch (IllegalArgumentException e) {
                return List.of();
            }
        }

        return tickets.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private Status convertToStatus(String statusStr) throws IllegalArgumentException {
        String upperCaseStatus = statusStr.toUpperCase();

        return Status.valueOf(upperCaseStatus);
    }

    private TicketResponseDTO toDto(Ticket ticket) {
        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setId(ticket.getId());
        dto.setTitulo(ticket.getTitulo() != null ? ticket.getTitulo().toString() : null);
        dto.setDescricao(ticket.getDescricao());
        dto.setDataCriacao(ticket.getDataCriacao());
        dto.setPrioridade(ticket.getPrioridade() != null ? ticket.getPrioridade().toString() : null);
        dto.setStatus(ticket.getStatus() != null ? ticket.getStatus().toString() : null);
        dto.setResposta(ticket.getResposta());

        if (ticket.getCliente() != null) {
            dto.setClienteId(ticket.getCliente().getId());
            dto.setNomeCliente(ticket.getCliente().getNome());
        }

        if (ticket.getTecnicoAtribuido() != null) {
            dto.setTecnicoId(ticket.getTecnicoAtribuido().getId());
            dto.setNomeTecnico(ticket.getTecnicoAtribuido().getNome());
        }

        return dto;
    }
    @Transactional
    public void atualizarStatus(Long ticketId, String novoStatusStr, Usuario tecnico) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket não encontrado."));

        Status novoStatus;
        try {
            novoStatus = Status.valueOf(novoStatusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Status inválido: " + novoStatusStr);
        }

        if (novoStatus == Status.EM_ANDAMENTO) {
            if (ticket.getStatus() != Status.PENDENTE) {
                throw new ConflictException("Não é possível iniciar um ticket que já está em " + ticket.getStatus());
            }
            ticket.setTecnicoAtribuido(tecnico);

        } else if (novoStatus == Status.CONCLUIDO) {
            if (ticket.getStatus() != Status.EM_ANDAMENTO) {
                throw new ConflictException("Um ticket só pode ser concluído se estiver 'Em Andamento'.");
            }
        } else {
            throw new BadRequestException("Transição de status inválida para: " + novoStatusStr);
        }

        ticket.setStatus(novoStatus);

        ticketRepository.save(ticket);
    }
    public TicketResponseDTO buscarTicketPorId(Long ticketId, Long userId, Perfil perfil) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket não encontrado."));

        if (perfil.equals(Perfil.ROLE_CLIENTE) && !ticket.getCliente().getId().equals(userId)) {
            throw new ForbiddenException("Acesso negado. Este ticket não pertence a este cliente.");
        }
        return toDto(ticket);
    }
    @Transactional
    public void responderTicket(Long ticketId, String resposta, Usuario tecnico) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket não encontrado."));

        if (ticket.getTecnicoAtribuido() == null) {
            ticket.setTecnicoAtribuido(tecnico);

        } else if (!ticket.getTecnicoAtribuido().getId().equals(tecnico.getId())) {
            throw new ForbiddenException("Ação proibida. Este ticket está em atendimento por outro técnico.");
        }

        if (ticket.getStatus() == Status.CONCLUIDO) {
            throw new BadRequestException("Não é possível responder/concluir um ticket que já está finalizado.");
        }
        if (resposta == null || resposta.trim().isEmpty()) {
            throw new ConflictException("A resposta do técnico não pode ser vazia.");
        }

        ticket.setResposta(resposta);
        ticket.setStatus(Status.CONCLUIDO);

        ticketRepository.save(ticket);
    }
    @Transactional
    public void excluirTicket(Long ticketId, Long clienteId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket não encontrado."));

        if (!ticket.getCliente().getId().equals(clienteId)) {
            throw new ForbiddenException("Acesso negado. Você não é o dono deste ticket.");
        }

        Status statusAtual = ticket.getStatus();

        if (statusAtual != Status.PENDENTE) {
            throw new BadRequestException("O ticket só pode ser excluído se o status for PENDENTE. Status atual: " + statusAtual);
        }

        ticketRepository.delete(ticket);
    }
    public List<TicketResponseDTO> buscarTicketsRespondidos(Long tecnicoId) {

        Usuario usuario = usuarioRepository.findById(tecnicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Técnico não existe"));

        if (usuario.getPerfil() != Perfil.ROLE_TECNICO) {
            throw new BusinessRuleException("Usuário não possui perfil de técnico");
        }
        List<Ticket> tickets = ticketRepository.findByStatusAndTecnicoResponsavel(Status.CONCLUIDO, tecnicoId);

        return tickets.stream()
                .map(ticket -> {
                    TicketResponseDTO dto = new TicketResponseDTO();
                    dto.setId(ticket.getId());
                    dto.setTitulo(ticket.getTitulo() != null ? ticket.getTitulo().name() : null);
                    dto.setDescricao(ticket.getDescricao());
                    dto.setDataCriacao(ticket.getDataCriacao());
                    dto.setStatus(ticket.getStatus() != null ? ticket.getStatus().name() : null);
                    dto.setPrioridade(ticket.getPrioridade() != null ? ticket.getPrioridade().name() : null);

                    if (ticket.getCliente() != null) {
                        dto.setClienteId(ticket.getCliente().getId());
                        dto.setNomeCliente(ticket.getCliente().getNome());
                    }

                    if (ticket.getTecnicoAtribuido() != null) {
                        dto.setTecnicoId(ticket.getTecnicoAtribuido().getId());
                        dto.setNomeTecnico(ticket.getTecnicoAtribuido().getNome());
                    }

                    dto.setResposta(ticket.getResposta());

                    return dto;
                })
                .collect(Collectors.toList());
    }

}

