package com.luizcarlos.omegatechapi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luizcarlos.omegatechapi.config.exception.ExternalServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OpenAiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OpenAiService openAiService;

    @Test
    void deveRetornarRespostaQuandoOpenAiResponderCorretamente() {

        Map<String, Object> message = Map.of("content", "Resposta da IA");
        Map<String, Object> choice = Map.of("message", message);
        Map<String, Object> body = Map.of("choices", List.of(choice));

        ResponseEntity<Map> responseEntity =
                new ResponseEntity<>(body, HttpStatus.OK);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenReturn(responseEntity);

        String resposta = openAiService.enviarParaOpenAI("Teste");

        assertEquals("Resposta da IA", resposta);
    }

    @Test
    void deveLancarExcecaoQuandoOpenAiFalhar() {

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenThrow(new RuntimeException());

        assertThrows(ExternalServiceException.class, () -> {
            openAiService.enviarParaOpenAI("Teste");
        });
    }
}
