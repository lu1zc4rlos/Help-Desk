package com.luizcarlos.omegatechapi.controller;

import com.luizcarlos.omegatechapi.model.dto.ChatResponse;
import com.luizcarlos.omegatechapi.model.request.MensagemRequest;
import com.luizcarlos.omegatechapi.model.entity.Usuario;
import com.luizcarlos.omegatechapi.service.ChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@Tag(name = "Chat", description = "Endpoints responsáveis pela comunicação entre usuários e chat integrado a OpenAI")
public class ChatController {

    @Autowired
    private final ChatService chatService;

    @PostMapping("/mensagem")
    public ResponseEntity<ChatResponse> enviarMensagem(
            @RequestBody MensagemRequest request,
            @AuthenticationPrincipal Usuario usuarioAutenticado) {

        ChatResponse resposta = chatService.processarMensagem(
                request.getMensagem(),
                usuarioAutenticado
        );
        return ResponseEntity.ok(resposta);
    }
}
