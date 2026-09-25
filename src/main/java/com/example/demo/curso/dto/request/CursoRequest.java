package com.example.demo.curso.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos técnicos para el registro o modificación de un curso en el catálogo")
public class CursoRequest {

    @Schema(description = "Identificador UUID opcional del curso", example = "c0000001-0000-4000-8000-000000000001")
    private String id;

    @Schema(description = "Nombre formal del curso", example = "Desarrollo Web con HTML5, CSS3 y JavaScript", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del curso es obligatorio")
    @Size(max = 255, message = "El nombre no puede superar los 255 caracteres")
    private String nombre;

    @Schema(description = "Descripción rica detallando prerrequisitos, temario y herramientas para embeddings en Qdrant", example = "Aprende los fundamentos para crear páginas web interactivas...", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La descripción del curso es obligatoria")
    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    private String descripcion;

    @Schema(description = "Identificador de la categoría asociada", example = "cat-001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El identificador de la categoría es obligatorio")
    private String categoriaId;

    @Schema(description = "Identificador del nivel de dificultad asociado", example = "niv-001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El identificador del nivel de dificultad es obligatorio")
    private String nivelId;

    @Schema(description = "Duración en horas académicas (entero positivo)", example = "40", minimum = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1, message = "La duración debe ser mayor que cero")
    private Integer duracion;

    @Schema(description = "Estado de vigencia del curso en el catálogo (activo o inactivo)", example = "true")
    private Boolean estado = true;
}
