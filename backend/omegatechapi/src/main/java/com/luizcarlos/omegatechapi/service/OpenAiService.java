package com.luizcarlos.omegatechapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luizcarlos.omegatechapi.config.exception.ExternalServiceException;
import com.luizcarlos.omegatechapi.model.dto.ChatIntentDTO;
import com.luizcarlos.omegatechapi.model.dto.TicketResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public String enviarParaOpenAI(String prompt) {

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o-mini");
        body.put("messages", List.of(
                Map.of("role", "system", "content", "Você é um assistente virtual especializado em suporte técnico da empresa OmegaTech. \n" +
                        "Seu papel é ajudar os usuários com dúvidas relacionadas a problemas técnicos, status de chamados, suporte a sistemas, falhas, erros e demais questões relacionadas ao atendimento técnico.\n" +
                        "\n" +
                        "Regras de conduta:\n" +
                        "- Responda **apenas** perguntas que estejam dentro do contexto de suporte técnico. \n" +
                        "- Se o usuário fizer uma pergunta fora desse escopo (como curiosidades, piadas, opiniões, assuntos pessoais, política, etc.), responda educadamente que você foi projetado apenas para auxiliar em suporte técnico.\n" +
                        "- Utilize um tom profissional, empático e objetivo.\n" +
                        "- Se o usuário perguntar sobre dados específicos (como \"qual foi meu último chamado?\"), utilize os dados fornecidos pela API para formular uma resposta clara.\n" +
                        "- Se os dados não estiverem disponíveis, diga que não há informações no momento.\n" +
                        "- Nunca invente informações, nunca crie números de protocolo ou respostas genéricas que possam induzir ao erro.\n" +
                        "\n" +
                        "Seu objetivo é ser preciso, educado e técnico.\n"),
                Map.of("role", "user", "content", prompt)
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
            if (choices == null || choices.isEmpty()) {
                throw new ExternalServiceException("Resposta inválida da OpenAI");
            }

            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

            return message.get("content").toString().trim();
        } catch (Exception e) {
            throw new ExternalServiceException("Falha na comunicação com a OpenAI");
        }
    }

    public ChatIntentDTO classificarIntencao(String mensagem){
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o-mini");
        body.put("temperature", 0.0);
        body.put("messages", List.of(
                Map.of("role", "system", "content", """
                Você é um classificador de intenções da assistente virtual da empresa OmegaTech.

                Sua função NÃO é responder o usuário.
                Sua única função é CLASSIFICAR a intenção da mensagem.

                Você DEVE responder APENAS com JSON válido.
                Nunca explique.
                Nunca escreva texto fora do JSON.
                Nunca use markdown.
                Nunca use ```.

                Intenções possíveis:

                1) CONSULTAR_TICKETS
                   Quando o usuário quiser:
                   - consultar chamados
                   - verificar status
                   - listar tickets
                   - saber informações sobre seus atendimentos
                   - perguntar sobre último, todos, pendentes, concluídos etc.

                   Retorne:
                   {
                     "intent": "CONSULTAR_TICKETS",
                     "status": "PENDENTE | EM_ANDAMENTO | CONCLUIDO | TODOS",
                     "ordenacao": "DATA_ASC | DATA_DESC"
                   }

                   Regras:
                   - Se não especificar status, use "TODOS"
                   - Se não especificar ordenação, use "DATA_DESC"

                2) PERGUNTA_TECNICA
                   Quando o usuário:
                   - fizer perguntas técnicas
                   - relatar problemas
                   - pedir ajuda com erro
                   - fizer saudações (oi, olá, bom dia)
                   - enviar mensagens genéricas
                   - fizer qualquer pergunta fora de consulta de tickets

                   Retorne:
                   {
                     "intent": "PERGUNTA_TECNICA"
                   }

                IMPORTANTE:
                Toda mensagem deve ser classificada em uma dessas duas intenções.
                Nunca retorne vazio.
                Nunca invente novas intenções.
                """),
                Map.of("role", "user", "content", mensagem)
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
            if (choices == null || choices.isEmpty()) {
                throw new ExternalServiceException("Resposta inválida da OpenAI");
            }

            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

            String json = message.get("content").toString().trim();

            return objectMapper.readValue(json, ChatIntentDTO.class);
        } catch (Exception e) {
            throw new ExternalServiceException("Falha ao classificar intenção");
        }
    }

    public String gerarRespostaComContexto(String pergunta, List<TicketResponseDTO> tickets) {

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o-mini");
        body.put("messages", List.of(
                Map.of("role", "system", "content",
                        "Você é um analista de suporte técnico da OmegaTech.\n" +
                                "\n" +
                                "Sua função é analisar os tickets fornecidos e responder ao usuário de forma natural, humana e profissional.\n" +
                                "\n" +
                                "Regras importantes:\n" +
                                "- Escreva como um atendente real explicando a situação.\n" +
                                "- NÃO use listas numeradas.\n" +
                                "- NÃO use marcadores, asteriscos ou formatação Markdown.\n" +
                                "- NÃO use símbolos como **, -, ou quebras de linha excessivas.\n" +
                                "- Escreva em formato de parágrafo fluido.\n" +
                                "- Seja claro, organizado e objetivo.\n" +
                                "\n" +
                                "Se houver até 5 tickets, explique cada um resumidamente dentro do texto.\n" +
                                "Se houver mais de 5 tickets, faça um resumo geral e destaque apenas os mais recentes.\n" +
                                "\n" +
                                "Se o usuário pedir detalhes específicos, explique com mais profundidade.\n" +
                                "\n" +
                                "Nunca invente informações.\n" +
                                "Se não houver tickets, informe de maneira educada que não há chamados registrados no momento.\n" +
                                "Finalize sempre oferecendo ajuda adicional ao usuário."
                ),
                Map.of("role", "user", "content",
                        "Pergunta do usuário:\n" + pergunta +
                                "\n\nTickets do usuário em formato JSON:\n" + tickets
                )
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
            if (choices == null || choices.isEmpty()) {
                throw new ExternalServiceException("Resposta inválida da OpenAI");
            }

            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

            return message.get("content").toString().trim();
        } catch (Exception e) {
            throw new ExternalServiceException("Falha ao gerar resposta com contexto");
        }
    }

}
