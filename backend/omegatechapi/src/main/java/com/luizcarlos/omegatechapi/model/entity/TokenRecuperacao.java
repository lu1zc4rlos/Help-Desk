package com.luizcarlos.omegatechapi.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tokens_recuperacao")
public class TokenRecuperacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false, name = "data_expiracao")
    private LocalDateTime dataExpiracao;

    @Column(name = "validado", nullable = false)
    private boolean validado = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Construtores
    public TokenRecuperacao() { }

    public TokenRecuperacao(String codigo, LocalDateTime dataExpiracao, Usuario usuario) {
        this.codigo = codigo;
        this.dataExpiracao = dataExpiracao;
        this.usuario = usuario;
    }
}
