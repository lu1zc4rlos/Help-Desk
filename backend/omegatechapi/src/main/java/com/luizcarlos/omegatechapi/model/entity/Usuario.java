package com.luizcarlos.omegatechapi.model.entity;

import com.luizcarlos.omegatechapi.model.enums.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Schema(description = "Dados do usuário")
@Data
@Entity
@Table(name = "usuarios_perfis")

public class Usuario implements UserDetails {
    @Schema(hidden = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(example = "Usuário")
    private String nome;
    @Schema(example = "2000-03-19")
    private LocalDate dataNascimento;
    @Schema(example = "usuario@email.com")
    private String email;
    @Schema(example = "123456")
    private String senha;

    @Schema(hidden = true)
    @Enumerated(EnumType.STRING)
    private Perfil perfil;

    @Schema(hidden = true)
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TecnicoProfile tecnicoProfile;

    @Schema(hidden = true)
    @Override
    public String getUsername() {
        return this.email;
    }

    @Schema(hidden = true)
    @Override
    public String getPassword() {
        return this.senha;
    }

    @Schema(hidden = true)
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Schema(hidden = true)
    @Override
    public boolean isAccountNonLocked() { return true; }

    @Schema(hidden = true)
    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Schema(hidden = true)
    @Override
    public boolean isEnabled() { return true; }

    @Schema(hidden = true)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.perfil == null) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority(this.perfil.name()));
    }


}
