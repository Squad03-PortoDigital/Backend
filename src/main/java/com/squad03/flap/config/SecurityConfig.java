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
@EnableMethodSecurity  // ✅ ATIVA @PreAuthorize
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

                // ✅ CORS - AGORA CORRETO
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ✅ CSRF DESABILITADO
                .csrf(csrf -> csrf.disable())

                // ✅ SESSÃO - IF_REQUIRED mantém compatibilidade com ambos
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                // ✅ AUTORIZAÇÃO - REMOVEU DUPLICAÇÃO
                .authorizeHttpRequests(auth -> auth
                        // ========== ROTAS PÚBLICAS ==========
                        .requestMatchers(HttpMethod.POST, "/usuarios/cadastro").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuarios/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuarios/me").permitAll()  // ✅ /me é público
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()

                        // ========== ROTAS QUE PRECISAM DE AUTENTICAÇÃO ==========
                        // A autorização específica é feita com @PreAuthorize nos Controllers
                        .requestMatchers("/tarefas/**").authenticated()
                        .requestMatchers("/empresas/**").authenticated()
                        .requestMatchers("/usuarios/**").authenticated()
                        .requestMatchers("/listas/**").authenticated()
                        .requestMatchers("/roles/**").authenticated()
                        .requestMatchers("/checklists/**").authenticated()
                        .requestMatchers("/comentarios/**").authenticated()
                        .requestMatchers("/equipes/**").authenticated()
                        .requestMatchers("/eventos/**").authenticated()
                        .requestMatchers("/itens/**").authenticated()
                        .requestMatchers("/membros/**").authenticated()
                        .requestMatchers("/permissoes/**").authenticated()
                        .requestMatchers("/notificacoes/**").authenticated()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/app/**").permitAll()
                        .requestMatchers("/topic/**").permitAll()
                        .requestMatchers("/queue/**").permitAll()
                        .requestMatchers("/google/**").permitAll()
                        // ========== QUALQUER OUTRA ROTA ==========
                        .anyRequest().authenticated()
                )

                // ✅ FORM LOGIN - MANTÉM SEGURANÇA
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

                // ✅ LOGOUT
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

                // ✅ HABILITA BASIC AUTH (para requisições via token)
                .httpBasic(basic -> basic
                        .realmName("Flap API")
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ✅ ORIGENS PERMITIDAS
        configuration.setAllowedOriginPatterns(List.of(
                "https://flap.gabrielfiel.com.br",
                "https://staging.d2d3xjpdbpom8h.amplifyapp.com",
                "http://localhost:3000",
                "http://localhost:5173",
                "http://127.0.0.1:5173",
                "http://127.0.0.1:3000",
                "http://*:5173",  // ✅ Qualquer IP na porta 5173
                "http://*:3000"   // ✅ Qualquer IP na porta 3000
        ));

        // ✅ MÉTODOS PERMITIDOS
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // ✅ HEADERS CRÍTICOS
        configuration.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin"
        ));

        // ✅ CREDENCIAIS - ESSENCIAL PARA COOKIES/SESSÃO
        configuration.setAllowCredentials(true);

        // ✅ TEMPO DE CACHE
        configuration.setMaxAge(3600L);

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
