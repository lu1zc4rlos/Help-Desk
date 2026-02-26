package com.luizcarlos.omegatechapi.service;

import com.luizcarlos.omegatechapi.config.exception.BadRequestException;
import com.luizcarlos.omegatechapi.model.dto.ChatIntentDTO;
import com.luizcarlos.omegatechapi.model.dto.ChatResponse;
import com.luizcarlos.omegatechapi.model.dto.TicketResponseDTO;
import com.luizcarlos.omegatechapi.model.entity.Ticket;
import com.luizcarlos.omegatechapi.model.enums.Prioridade;
import com.luizcarlos.omegatechapi.model.enums.Status;
import com.luizcarlos.omegatechapi.model.enums.TipoProblema;
import com.luizcarlos.omegatechapi.model.enums.TipoResposta;
import com.luizcarlos.omegatechapi.model.entity.Usuario;
import com.luizcarlos.omegatechapi.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final OpenAiService openAiService;
    private final TicketRepository ticketRepository;

    public ChatResponse processarMensagem(String mensagem, Usuario usuario) {

        if(mensagem == null || mensagem.isBlank()){
            throw new BadRequestException("Mensagem não pode ser vazia");
        }

        ChatResponse response = new ChatResponse();
        response.setTimestamp(LocalDate.now());

        ChatIntentDTO intent = openAiService.classificarIntencao(mensagem);

        if(intent.getIntent().equals("CONSULTAR_TICKETS")) {

            Status status = Status.valueOf(intent.getStatus());
            response.setTipo(TipoResposta.CONSULTA_BD);

                    if(status == Status.TODOS){
                        List<Ticket> tickets = ticketRepository.findByClienteId(usuario.getId());
                        List<TicketResponseDTO> ticketResponseDTO = tickets.stream()
                                .map(this::converterParaDTO)
                                .toList();

                        String respostaFinal = openAiService.gerarRespostaComContexto(
                                mensagem,
                                ticketResponseDTO
                        );
                        response.setTickets(null);
                        response.setResposta(respostaFinal);
                    }
                    else{
                        List<Ticket> tickets = ticketRepository.findByClienteIdAndStatus(usuario.getId(), status);
                        List<TicketResponseDTO> ticketResponseDTO = tickets.stream()
                                .map(this::converterParaDTO)
                                .toList();

                        String respostaFinal = openAiService.gerarRespostaComContexto(
                                mensagem,
                                ticketResponseDTO
                        );
                        response.setTickets(null);
                        response.setResposta(respostaFinal);
                    }

            return response;
        }
        else{
            String respostaGPT = openAiService.enviarParaOpenAI(mensagem);
            response.setTipo(TipoResposta.GPT);
            response.setResposta(respostaGPT);
            response.setTickets(null);
        }
        return response;
    }

    public String converterTipoProblema(TipoProblema valor) {
        try {
            return String.valueOf(valor);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Tipo do Problema inválido: " + valor);
        }
    }

    public String converterStatus(Status valor) {
        try {
            return String.valueOf(valor);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Status inválido: " + valor);
        }
    }

    public String converterPrioridade(Prioridade valor) {
        try {
            return String.valueOf(valor);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Prioridade inválida: " + valor);
        }
    }

    protected TicketResponseDTO converterParaDTO(Ticket ticket){

        String titulo = converterTipoProblema(ticket.getTitulo());
        String prioridade = converterPrioridade(ticket.getPrioridade());
        String status = converterStatus(ticket.getStatus());

        return new TicketResponseDTO(
                ticket.getId(),
                titulo,
                ticket.getDescricao(),
                ticket.getDataCriacao(),
                prioridade,
                status,
                ticket.getResposta(),
                ticket.getCliente().getId(),
                ticket.getCliente().getNome(),
                ticket.getTecnicoAtribuido().getId(),
                ticket.getTecnicoAtribuido().getNome()
        );
    }
}
