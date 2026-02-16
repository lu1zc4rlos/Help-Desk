package com.luizcarlos.omegatechapi.service;

import com.luizcarlos.omegatechapi.config.exception.UnauthorizedException;

import java.security.Principal;

public class AuthService {
    public static Long extractUserId(Principal principal) {
        if (principal == null) {
            throw new UnauthorizedException("Usuário não autenticado.");
        }

        String userIdStr = principal.getName();

        if (userIdStr == null || userIdStr.isEmpty()) {
            throw new UnauthorizedException("Token inválido");
        }

        try {
            return Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Token inválido.");
        }
    }
}
