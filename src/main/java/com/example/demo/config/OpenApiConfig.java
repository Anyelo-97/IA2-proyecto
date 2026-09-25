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
                        .description("API REST para el sistema de recomendación de cursos académicos mediante arquitectura RAG sincrónica.\n\n" +
                                "Seguridad y Roles:\n" +
                                "- Token JWT Bearer firmado (HMAC-SHA256). Iniciar sesión en POST /api/auth/login o registro público en POST /api/auth/register/estudiante.\n" +
                                "- Rol ESTUDIANTE: Puede consultar el catálogo público, ejecutar consultas inteligentes en lenguaje natural (/api/consultas), calificar recomendaciones y acceder a su historial personal. " +
                                "El identificador del estudiante se extrae de forma segura desde el token JWT decodificado en el servidor para evitar suplantación de identidad.\n" +
                                "- Rol ADMINISTRADOR: Puede crear, actualizar y desactivar cursos (sincronizados con el vector store Qdrant vía n8n), gestionar categorías y administrar cuentas de estudiantes.\n\n" +
                                "Uso en Swagger: Presiona el botón 'Authorize' e introduce únicamente el token JWT obtenido en el login; Swagger agregará automáticamente el prefijo Bearer."))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT en formato Bearer (ingresar: Bearer <token>)")))
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
