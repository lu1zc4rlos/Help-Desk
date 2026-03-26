package com.luizcarlos.omegatechapi.config;

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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        List<String> origins = new ArrayList<>();

        if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
            origins.addAll(Arrays.asList(allowedOrigins.split(",")));
        }

        origins.add("https://seu-app.up.railway.app");

        config.setAllowedOrigins(Arrays.asList("*"));

        config.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"
        ));

        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "x-auth-token",
                "*"
        ));

        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")


                        .requestMatchers("/usuarios/login", "/auth/login").permitAll()
                        .requestMatchers("/usuarios/cadastro").permitAll()

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
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        )
                        .permitAll()



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
