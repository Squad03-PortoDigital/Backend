package com.squad03.flap.config;

import com.squad03.flap.service.DetalhesUsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private DetalhesUsuarioServiceImpl detalhesUsuarioService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

                .authorizeHttpRequests(auth -> auth
                        // Rotas públicas
                        .requestMatchers(HttpMethod.POST, "/usuarios/cadastro").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/login").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/cargos", "/roles").permitAll()

                        // Rotas autenticadas
                        .requestMatchers(HttpMethod.GET, "/usuarios/me").authenticated()

                        // ✅ ADICIONAR: Operações de arquivamento (ANTES das rotas gerais de tarefas)
                        .requestMatchers(HttpMethod.GET, "/tarefas/arquivadas").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/tarefas/*/arquivar").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/tarefas/*/desarquivar").authenticated()

                        // Operações em tarefas
                        .requestMatchers(HttpMethod.GET, "/tarefas/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/tarefas").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/tarefas/**").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/tarefas/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/tarefas/**").authenticated()

                        // Operações em listas
                        .requestMatchers(HttpMethod.GET, "/listas/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/listas").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/listas/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/listas/**").authenticated()

                        // Empresas
                        .requestMatchers(HttpMethod.GET, "/empresas/**").authenticated()

                        // ✅ Usar hasRole() - Spring adiciona prefixo ROLE_ automaticamente
                        .requestMatchers(HttpMethod.GET, "/usuarios").hasRole("ADMINISTRADOR_MASTER")

                        // Operações individuais em usuários
                        .requestMatchers(HttpMethod.GET, "/usuarios/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/usuarios/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/**").hasRole("ADMINISTRADOR_MASTER")

                        // Todas as outras rotas
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginProcessingUrl("/usuarios/login")
                        .usernameParameter("email")
                        .passwordParameter("senha")
                        .successHandler(authenticationSuccessHandler())
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Falha na autenticação: Credenciais inválidas\"}");
                        })
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/usuarios/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\": \"Logout realizado com sucesso\"}");
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                .httpBasic(basic -> basic.disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Login efetuado com sucesso!\", \"authenticated\": true}");
        };
    }
}
