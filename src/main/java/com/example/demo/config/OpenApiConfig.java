package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI rutaIaOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("RutaIA API")
                        .description("API REST para recomendación de cursos mediante búsqueda semántica y RAG")
                        .version("v1.0"))
                .tags(List.of(
                        new Tag().name("Cursos").description("CRUD y catálogo de cursos"),
                        new Tag().name("Consultas").description("Consultas inteligentes con RAG"),
                        new Tag().name("Recomendaciones").description("Resultados y fuentes de recomendaciones"),
                        new Tag().name("Calificaciones").description("Calificación de recomendaciones"),
                        new Tag().name("Estadísticas").description("Estadísticas del sistema")
                ));
    }
}
