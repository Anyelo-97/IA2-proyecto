package com.example.demo.security;

import com.example.demo.exception.ErrorResponse;
import com.example.demo.security.jwt.JwtAuthenticationFilter;
import com.example.demo.util.PasswordUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;
    private final ObjectMapper jsonMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();

            @Override
            public String encode(CharSequence rawPassword) {
                return PasswordUtils.sha256(rawPassword.toString());
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                if (encodedPassword == null) return false;
                String rawSha256 = PasswordUtils.sha256(rawPassword.toString());
                return encodedPassword.equalsIgnoreCase(rawSha256)
                        || encodedPassword.equals(rawPassword.toString())
                        || bcrypt.matches(rawPassword, encodedPassword);
            }
        };
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding("UTF-8");
                            ErrorResponse error = new ErrorResponse(
                                    LocalDateTime.now(),
                                    HttpStatus.UNAUTHORIZED.value(),
                                    "Token no proporcionado o inválido",
                                    "UNAUTHORIZED"
                            );
                            jsonMapper.writeValue(response.getOutputStream(), error);
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            response.setCharacterEncoding("UTF-8");
                            ErrorResponse error = new ErrorResponse(
                                    LocalDateTime.now(),
                                    HttpStatus.FORBIDDEN.value(),
                                    "Acceso denegado: permisos insuficientes",
                                    "FORBIDDEN"
                            );
                            jsonMapper.writeValue(response.getOutputStream(), error);
                        })
                )
                .authorizeHttpRequests(auth -> auth
                        // Rutas publicas de consulta general y documentacion
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/cursos/**", "/api/catalogo/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categorias/**", "/api/niveles-dificultad/**", "/api/niveles/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Rutas administrativas exclusivas (alta, edicion, desactivacion de cursos, categorias y gestion global de estudiantes)
                        .requestMatchers(HttpMethod.POST, "/api/cursos/**", "/api/categorias/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/cursos/**", "/api/categorias/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PATCH, "/api/cursos/**", "/api/categorias/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/cursos/**", "/api/categorias/**").hasRole("ADMINISTRADOR")

                        // Gestion administrativa de estudiantes (listar todos, crear, eliminar)
                        .requestMatchers(HttpMethod.GET, "/api/estudiantes").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/estudiantes/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.PUT, "/api/estudiantes/**").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/estudiantes/**").hasRole("ADMINISTRADOR")

                        // Consulta de perfil individual de estudiante (el estudiante consulta su propio perfil, admin cualquiera)
                        .requestMatchers(HttpMethod.GET, "/api/estudiantes/{id}").hasAnyRole("ESTUDIANTE", "ADMINISTRADOR")

                        // Rutas de estudiantes o administradores autenticados (consultas RAG, calificaciones, estadisticas y /me)
                        .requestMatchers("/api/consultas/**", "/api/calificaciones/**", "/api/estadisticas/**", "/api/auth/me").hasAnyRole("ESTUDIANTE", "ADMINISTRADOR")

                        // Cualquier otra peticion requiere autenticacion
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
