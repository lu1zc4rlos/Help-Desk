package com.luizcarlos.omegatechapi.config.exception;

public class ConflictException extends RuntimeException{

    public ConflictException(String mensagem){
        super(mensagem);
    }
}
