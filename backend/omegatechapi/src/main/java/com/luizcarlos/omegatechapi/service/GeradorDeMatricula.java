package com.luizcarlos.omegatechapi.service;

import java.util.Random;

public class GeradorDeMatricula {
    private static final String CARACTERES_PERMITIDOS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int TAMANHO_DO_CODIGO = 7;
    private static final Random RANDOM = new Random();

    public static String gerarMatricula() {

        StringBuilder codigo = new StringBuilder(TAMANHO_DO_CODIGO);

        for (int i = 0; i < TAMANHO_DO_CODIGO; i++) {
            int indiceAleatorio = RANDOM.nextInt(CARACTERES_PERMITIDOS.length());

            codigo.append(CARACTERES_PERMITIDOS.charAt(indiceAleatorio));
        }

        return codigo.toString();
    }
}
