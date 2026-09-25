package com.example.demo.curso.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos generales de una consulta registrada")
public class ConsultaResponse {

    @Schema(description = "Identificador único de la consulta", example = "con-001")
    private String id;

    @Schema(description = "Identificador del estudiante solicitante", example = "est-001")
    private String estudianteId;

    @Schema(description = "Pregunta en lenguaje natural", example = "Quiero aprender programación backend con Spring")
    private String pregunta;

    @Schema(description = "Fecha y hora de creación de la consulta", example = "2026-09-24T18:00:00")
    private LocalDateTime fecha;

    @Schema(description = "Estado actual de la consulta (Pendiente, Respondida, Sin resultados, Error)", example = "Respondida")
    private String estado;
}
