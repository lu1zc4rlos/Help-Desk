package com.luizcarlos.omegatechapi.model.entity;

import com.luizcarlos.omegatechapi.model.enums.Prioridade;
import com.luizcarlos.omegatechapi.model.enums.Status;
import com.luizcarlos.omegatechapi.model.enums.TipoProblema;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Entity
@Table(name = "tickets")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoProblema titulo;

    @Column(nullable = false, length = 2000)
    private String descricao;

    @Column(name = "data_criacao", updatable = false)
    private LocalDate dataCriacao = LocalDate.now();

    @Enumerated(EnumType.STRING)
    public Prioridade prioridade;
    @Enumerated(EnumType.STRING)
    public Status status;

    public String Resposta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_id")
    private Usuario tecnicoAtribuido;
}
