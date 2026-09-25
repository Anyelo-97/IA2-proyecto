package com.example.demo.curso.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
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
@Schema(description = "Datos para calificar la utilidad y pertinencia de una recomendación académica")
public class CalificacionRequest {

    @Schema(description = "Identificador opcional de la calificación", example = "cal-001")
    private String id;

    @Schema(description = "Identificador del estudiante que emite la calificación (opcional si se proporciona en token)", example = "est-001")
    private String estudianteId;

    @Schema(description = "Identificador de la recomendación evaluada", example = "rec-001", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El identificador de la recomendación es obligatorio")
    private String recomendacionId;

    @Schema(description = "Puntuación numérica estricta entre 1 (mínimo) y 5 (máximo)", example = "5", minimum = "1", maximum = "5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1, message = "La puntuación mínima es 1")
    @Max(value = 5, message = "La puntuación máxima es 5")
    private Integer puntuacion;

    @Schema(description = "Comentario cualitativo opcional sobre la recomendación recibida", example = "La recomendación fue muy precisa y los cursos sugeridos se adaptaron a mi nivel.")
    @Size(max = 1000, message = "El comentario no puede superar los 1000 caracteres")
    private String comentario;
}
