package com.luizcarlos.omegatechapi.service;

import com.luizcarlos.omegatechapi.config.exception.InternalServerErrorException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    void deveEnviarEmailDeBoasVindas(){

        String email = "teste@email.com";
        String nome = "Luiz";

        emailService.enviarEmailDeBoasVindas(email,nome);
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void deveLancarExcecaoQuandoFalharEnvioTrocaDeSenha() {

        String email = "teste@email.com";

        doThrow(new RuntimeException())
                .when(mailSender)
                .send(any(SimpleMailMessage.class));

        assertThrows(InternalServerErrorException.class, () -> {
            emailService.enviarEmailTrocaDeSenha(email);
        });
    }

    @Test
    void deveLancarExcecaoQuandoFalharEnvioCodigo() {

        String email = "teste@email.com";

        doThrow(new RuntimeException())
                .when(mailSender)
                .send(any(SimpleMailMessage.class));

        assertThrows(InternalServerErrorException.class, () -> {
            emailService.enviarEmailCodigo(email, "123456");
        });
    }
}
