package com.example.demo.curso.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resultado del procesamiento sincrónico RAG con recomendación generada y cursos fuente")
public class ConsultaConResultadoResponse {

    @Schema(description = "Identificador único de la consulta registrada", example = "con-001")
    private String consultaId;

    @Schema(description = "Identificador único de la recomendación persistida", example = "rec-001")
    private String recomendacionId;

    @Schema(description = "Pregunta o necesidad expresada por el estudiante", example = "Quiero aprender desarrollo web con JavaScript")
    private String pregunta;

    @Schema(description = "Estado de resolución de la consulta (Respondida, Sin resultados, Error)", example = "Respondida")
    private String estado;

    @Schema(description = "Consejo académico personalizado generado por el LLM a partir de los cursos recuperados", example = "Te recomiendo iniciar con el curso de Desarrollo Web con HTML5, CSS3 y JavaScript...")
    private String respuesta;

    @Schema(description = "Listado de cursos del catálogo institucional utilizados como fuente semántica")
    private List<FuenteResponse> fuentes;

    public ConsultaConResultadoResponse(String consultaId, String pregunta, String estado, String respuesta, List<FuenteResponse> fuentes) {
        this.consultaId = consultaId;
        this.pregunta = pregunta;
        this.estado = estado;
        this.respuesta = respuesta;
        this.fuentes = fuentes;
    }

    @Schema(description = "Estructura de recomendación directa para compatibilidad frontend")
    public RecomendacionDetalleDto getRecomendacion() {
        if (recomendacionId == null && respuesta == null) return null;
        return new RecomendacionDetalleDto(recomendacionId, respuesta, fuentes);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Detalle anidado de la recomendación")
    public static class RecomendacionDetalleDto {
        private String id;
        private String contenido;
        private List<FuenteResponse> fuentes;
    }
}
