package com.example.demo.curso.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Registro del historial académico de consultas con detalles de recomendación y fuentes")
public class HistorialResponse {

    @Schema(description = "Identificador único de la consulta", example = "con-001")
    private String consultaId;

    @Schema(description = "Pregunta realizada por el estudiante", example = "Quiero aprender JavaScript para frontend")
    private String pregunta;

    @Schema(description = "Fecha y hora de la consulta", example = "2026-09-24T18:00:00")
    private LocalDateTime fecha;

    @Schema(description = "Estado de resolución (Respondida, Sin resultados, Error)", example = "Respondida")
    private String estado;

    @Schema(description = "Resumen o texto preliminar de la recomendación", example = "Te sugerimos el curso de Desarrollo Web con HTML5...")
    private String resumen;

    @Schema(description = "Nombres de los cursos recomendados vinculados", example = "[\"Desarrollo Web con HTML5, CSS3 y JavaScript\"]")
    private List<String> cursosRecomendados;

    @Schema(description = "Detalle anidado de la recomendación, fuentes y calificación para frontend")
    private RecomendacionHistorialDTO recomendacion;

    public HistorialResponse(String consultaId, String pregunta, LocalDateTime fecha, String estado, String resumen, List<String> cursosRecomendados) {
        this.consultaId = consultaId;
        this.pregunta = pregunta;
        this.fecha = fecha;
        this.estado = estado;
        this.resumen = resumen;
        this.cursosRecomendados = cursosRecomendados;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Información detallada de la recomendación en el historial")
    public static class RecomendacionHistorialDTO {
        @Schema(description = "Identificador de la recomendación", example = "rec-001")
        private String id;

        @Schema(description = "Contenido completo de la recomendación", example = "Para iniciar en desarrollo web te recomendamos...")
        private String contenido;

        @Schema(description = "Estado de la recomendación", example = "Respondida")
        private String estado;

        @Schema(description = "Listado de cursos fuente que sustentaron la recomendación")
        private List<FuenteResponse> fuentes;

        @Schema(description = "Calificación registrada por el estudiante si existe")
        private CalificacionSimpleDTO calificacion;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Puntuación y comentario de la calificación")
    public static class CalificacionSimpleDTO {
        @Schema(description = "Puntuación del 1 al 5", example = "5")
        private Integer puntuacion;

        @Schema(description = "Comentario cualitativo opcional", example = "Muy buena recomendación")
        private String comentario;
    }
}
