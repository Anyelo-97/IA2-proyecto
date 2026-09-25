package com.example.demo.curso.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Fuente de curso recuperada semánticamente en Qdrant y utilizada para la recomendación")
public class FuenteResponse {

    @Schema(description = "Identificador único de la fuente", example = "fue-001")
    private String id;

    @Schema(description = "Identificador de la recomendación asociada", example = "rec-001")
    private String recomendacionId;

    @Schema(description = "Identificador UUID del curso en el catálogo", example = "c0000002-0000-4000-8000-000000000002")
    private String cursoId;

    @Schema(description = "Nombre completo del curso", example = "Desarrollo Backend con Java y Spring Boot")
    private String cursoNombre;

    @Schema(description = "Descripción del contenido del curso", example = "Domina la creación de APIs REST robustas empleando Java 17 y Spring Boot 3...")
    private String cursoDescripcion;

    @Schema(description = "Nombre de la categoría del curso", example = "Desarrollo Web")
    private String categoriaNombre;

    @Schema(description = "Nivel de dificultad del curso", example = "Intermedio")
    private String nivelNombre;

    @Schema(description = "Duración del curso en horas académicas", example = "60")
    private Integer duracion;

    @Schema(description = "Puntuación de similitud semántica coseno (0.0 a 1.0)", example = "0.924")
    private Double similitud;

    public FuenteResponse(String id, String recomendacionId, String cursoId, String cursoNombre, String cursoDescripcion, String categoriaNombre, Double similitud) {
        this.id = id;
        this.recomendacionId = recomendacionId;
        this.cursoId = cursoId;
        this.cursoNombre = cursoNombre;
        this.cursoDescripcion = cursoDescripcion;
        this.categoriaNombre = categoriaNombre;
        this.similitud = similitud;
    }

    public String getNombre() {
        return cursoNombre;
    }

    public String getDescripcion() {
        return cursoDescripcion;
    }

    public String getCategoria() {
        return categoriaNombre;
    }

    public String getNivel() {
        return nivelNombre;
    }
}
