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
@Schema(description = "Ficha detallada de un curso del catálogo institucional")
public class CursoResponse {

    @Schema(description = "Identificador único UUID del curso", example = "c0000001-0000-4000-8000-000000000001")
    private String id;

    @Schema(description = "Nombre formal del curso", example = "Desarrollo Web con HTML5, CSS3 y JavaScript")
    private String nombre;

    @Schema(description = "Descripción rica del curso", example = "Aprende los fundamentos para crear páginas web interactivas...")
    private String descripcion;

    @Schema(description = "Identificador de la categoría", example = "cat-001")
    private String categoriaId;

    @Schema(description = "Nombre de la categoría asociada", example = "Desarrollo Web")
    private String categoriaNombre;

    @Schema(description = "Identificador del nivel de dificultad", example = "niv-001")
    private String nivelId;

    @Schema(description = "Nombre del nivel de dificultad asociado", example = "Básico")
    private String nivelNombre;

    @Schema(description = "Duración total en horas académicas", example = "40")
    private Integer duracion;

    @Schema(description = "Estado de vigencia del curso (true: activo, false: inactivo)", example = "true")
    private Boolean estado;

    public String getCategoria() {
        return categoriaNombre;
    }

    public String getNivel() {
        return nivelNombre;
    }
}
