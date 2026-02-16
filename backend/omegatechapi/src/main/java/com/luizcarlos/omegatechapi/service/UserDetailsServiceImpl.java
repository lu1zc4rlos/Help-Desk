package com.luizcarlos.omegatechapi.service;

import com.luizcarlos.omegatechapi.config.exception.ResourceNotFoundException;
import com.luizcarlos.omegatechapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String idOuEmail) throws UsernameNotFoundException {

        try {
            Long userId = Long.parseLong(idOuEmail);

            return usuarioRepository.findByUserId(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        } catch (NumberFormatException e) {

            return usuarioRepository.findByEmail(idOuEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        }
    }
}
