package com.luizcarlos.omegatechapi.service;

import com.luizcarlos.omegatechapi.config.exception.BadRequestException;
import com.luizcarlos.omegatechapi.model.dto.ChatResponse;
import com.luizcarlos.omegatechapi.model.enums.TipoResposta;
import com.luizcarlos.omegatechapi.model.entity.Usuario;
import lombok.RequiredArgsConstructor;
import com.luizcarlos.omegatechapi.repository.ChatRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chamadoRepository;
    private final OpenAiService openAiService;


    public ChatResponse processarMensagem(String mensagem, Usuario usuario) {

        if(mensagem == null || mensagem.isBlank()){
            throw new BadRequestException("Mensagem não pode ser vazia");
        }
        Long usuarioId = usuario.getId();

        ChatResponse response = new ChatResponse();
        response.setTimestamp(LocalDate.now());


            if (ehConsultaDeChamado(mensagem)) {
                Object dados = consultarChamado(mensagem, usuario.getId());

                response.setTipo(TipoResposta.CONSULTA_BD);
                response.setDados(dados);
                response.setResposta(gerarResumoGPT(mensagem, dados));
            }
            else {
                String respostaGPT = openAiService.enviarParaOpenAI(mensagem);
                response.setTipo(TipoResposta.GPT);
                response.setResposta(respostaGPT);
            }

        return response;
    }


    private boolean ehConsultaDeChamado(String mensagem) {
        String texto = mensagem.toLowerCase();

        return texto.contains("chamado") ||
                texto.contains("ticket") ||
                texto.contains("atendimento");
    }

    private Object consultarChamado(String mensagem, Long usuarioId) {
        String texto = mensagem.toLowerCase();

        if (texto.contains("último") || texto.contains("ultimo"))
            return chamadoRepository.findUltimoChamadoPorUsuario(usuarioId);
        else if (texto.contains("todos") || texto.contains("meus"))
            return chamadoRepository.findChamadosPorUsuario(usuarioId);
        else
            return chamadoRepository.findChamadosRecentes(usuarioId);
    }


    private String gerarResumoGPT(String pergunta, Object dados) {
        String prompt = "Resuma em uma frase amigável a resposta do banco para a pergunta: '"
                + pergunta + "'.\n\nDados: " + dados.toString();

        return openAiService.enviarParaOpenAI(prompt);
    }
}
