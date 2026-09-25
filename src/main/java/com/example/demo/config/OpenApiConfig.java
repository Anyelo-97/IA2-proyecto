package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI rutaIaOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("RutaIA API")
                        .version("1.0.0")
                        .description("API REST para el sistema de recomendacion de cursos academicos mediante arquitectura RAG."))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT en formato Bearer")))
                .tags(List.of(
                        new Tag().name("0. Autenticación y Cuentas").description("Inicio de sesión, registro y perfiles de usuarios."),
                        new Tag().name("1. Consultas Inteligentes (RAG)").description("Pipeline RAG e historial de consultas."),
                        new Tag().name("2. Calificaciones").description("Evaluacion de recomendaciones academicas."),
                        new Tag().name("3. Catálogo y Cursos").description("Gestion y consulta de cursos academicos."),
                        new Tag().name("4. Estudiantes").description("Registro y perfiles de estudiantes."),
                        new Tag().name("5. Estadísticas del Sistema").description("Metricas y analiticas generales."),
                        new Tag().name("6. Catálogos de Referencia").description("Catalogos de categorias y niveles de dificultad.")
                ));
    }
}
