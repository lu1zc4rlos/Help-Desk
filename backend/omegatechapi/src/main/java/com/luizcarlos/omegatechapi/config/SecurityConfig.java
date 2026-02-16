package com.luizcarlos.omegatechapi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Value("${api.security.cors.origins:http://localhost:5173}")
    private String allowedOrigins;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthFilter,
            AuthenticationProvider authenticationProvider,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint
    ) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Configuração Explicita do CORS ligada ao Bean lá embaixo
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 2. Libera o Preflight (OPTIONS) para qualquer rota
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Rotas Públicas
                        .requestMatchers("/usuarios/login", "/auth/login").permitAll()
                        .requestMatchers("/usuarios/cadastro").permitAll()
                        // ... suas outras rotas ...
                        .requestMatchers(HttpMethod.PUT, "/usuarios/alterar_senha").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/solicitar_codigo").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/validar_codigo").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/usuarios/resetar_senha").permitAll()
                        .requestMatchers(HttpMethod.POST, "/chat/mensagem").permitAll()
                        .requestMatchers(HttpMethod.POST, "/tickets/criar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/tickets/meus").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/tickets/status").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/tickets/resposta").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/tickets/deletar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/admin/cadastro").permitAll()
                        .requestMatchers(HttpMethod.GET, "/admin/tecnicos").permitAll()
                        .requestMatchers(HttpMethod.GET, "/admin/respondidos").permitAll()

                        // O resto precisa de token
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();


        // Se der erro, troque "," por "http://localhost:5173"
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "x-auth-token"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
