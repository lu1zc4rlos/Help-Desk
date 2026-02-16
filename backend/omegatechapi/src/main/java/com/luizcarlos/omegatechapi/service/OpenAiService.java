package com.luizcarlos.omegatechapi.service;

import com.luizcarlos.omegatechapi.config.exception.ExternalServiceException;
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
}
