package com.luizcarlos.omegatechapi.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.*;

public class GeradorDeMatriculaTest {

    @InjectMocks
    private GeradorDeMatricula geradorDeMatricula;

    @Test
    void deveGerarMatriculaCom7Caracteres(){
        String matricula = geradorDeMatricula.gerarMatricula();

        assertNotNull(matricula);
        assertEquals(7,matricula.length());
    }

    @Test
    void deveConterApenasCaracteresPermitidos(){
        String matricula = geradorDeMatricula.gerarMatricula();

        assertTrue(matricula.matches("^[A-Z0-9]{7}$"));
    }

    @Test
    void deveGerarCodigosDiferentes(){
        String codigo1 = geradorDeMatricula.gerarMatricula();
        String codigo2 = geradorDeMatricula.gerarMatricula();
        assertNotEquals(codigo1,codigo2);
    }
}
