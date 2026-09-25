package com.example.demo.curso.controller;

import com.example.demo.curso.dto.response.EstadisticasResponse;
import com.example.demo.curso.service.EstadisticaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "5. Estadísticas del Sistema", description = "Métricas analíticas consolidadas en tiempo real sobre el rendimiento y uso de la plataforma.")
@RestController
@RequestMapping("/api/estadisticas")
@RequiredArgsConstructor
public class EstadisticaController {

    private final EstadisticaService estadisticaService;

    @Operation(
            summary = "Dashboard de métricas globales (consultas totales, respondidas, sin resultados, promedio de calificaciones, curso más recomendado)",
            description = "Calcula y consolida en tiempo real las métricas analíticas clave del sistema: "
                    + "total de consultas recibidas, consultas con recomendación exitosa, consultas sin resultados afines en el catálogo, "
                    + "promedio general de calificaciones (escala 1 a 5) y el curso más recomendado por el modelo RAG."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Métricas globales consolidadas calculadas exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstadisticasResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno al calcular las métricas estadísticas", content = @Content)
    })
    @GetMapping
    public ResponseEntity<EstadisticasResponse> obtenerEstadisticas() {
        return ResponseEntity.ok(estadisticaService.obtenerEstadisticas());
    }
}
