package com.luizcarlos.omegatechapi.config;

import com.luizcarlos.omegatechapi.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationProvider authenticationProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Value("${api.security.cors.origins:http://localhost:5173}")
    private String allowedOrigins;

    public SecurityConfig(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            AuthenticationProvider authenticationProvider,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.authenticationProvider = authenticationProvider;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "x-auth-token", "*"));
        config.setAllowCredentials(false); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthFilter = new JwtAuthenticationFilter(jwtService, userDetailsService);

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 1. Rotas Públicas Críticas no Topo
                        .requestMatchers("/usuarios/login", "/usuarios/cadastro").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // 2. Outras rotas permitidas
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

                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                );

        return http.build();
    }
}