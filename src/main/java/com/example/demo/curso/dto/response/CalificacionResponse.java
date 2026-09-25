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
@Schema(description = "Respuesta detallada con los datos de una calificación registrada")
public class CalificacionResponse {

    @Schema(description = "Identificador único de la calificación", example = "cal-001")
    private String id;

    @Schema(description = "Identificador del estudiante que emitió la calificación", example = "est-001")
    private String estudianteId;

    @Schema(description = "Identificador de la recomendación calificada", example = "rec-001")
    private String recomendacionId;

    @Schema(description = "Puntuación otorgada del 1 al 5", example = "5")
    private Integer puntuacion;

    @Schema(description = "Comentario cualitativo asociado", example = "Excelente recomendación, muy clara.")
    private String comentario;
}
