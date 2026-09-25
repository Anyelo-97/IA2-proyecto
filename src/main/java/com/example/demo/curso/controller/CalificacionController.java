package com.example.demo.curso.controller;

import com.example.demo.curso.dto.request.CalificacionRequest;
import com.example.demo.curso.dto.response.CalificacionResponse;
import com.example.demo.curso.service.CalificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.example.demo.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "2. Calificaciones", description = "Registro y consulta de evaluaciones de satisfacción y pertinencia sobre las recomendaciones académicas emitidas.")
@RestController
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
public class CalificacionController {

    private final CalificacionService calificacionService;

    @Operation(
            summary = "Calificar una recomendación académica",
            description = "Permite al estudiante calificar la utilidad y pertinencia de una recomendación académica recibida. "
                    + "Valida estrictamente que la puntuación sea un número entero entre 1 y 5, permite registrar un comentario opcional "
                    + "y aplica la regla de negocio de una única calificación por recomendación (el intento de duplicar es rechazado con error 400)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Calificación registrada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CalificacionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Puntuación fuera de rango (1 a 5) o la recomendación ya fue calificada previamente", content = @Content),
            @ApiResponse(responseCode = "404", description = "Estudiante o recomendación no encontrada con el identificador proporcionado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CalificacionResponse> calificar(
            @Valid @RequestBody CalificacionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null && userDetails.getId() != null) {
            request.setEstudianteId(userDetails.getId());
        }
        CalificacionResponse response = calificacionService.calificar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Consultar la calificación asignada a una recomendación",
            description = "Recupera la puntuación y comentarios asignados a una recomendación académica específica a partir del identificador de dicha recomendación."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Calificación encontrada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CalificacionResponse.class))),
            @ApiResponse(responseCode = "404", description = "No existe calificación para la recomendación especificada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/{recomendacionId}")
    public ResponseEntity<CalificacionResponse> obtenerPorRecomendacion(
            @Parameter(description = "Identificador único de la recomendación evaluada", example = "rec-001") @PathVariable String recomendacionId) {
        CalificacionResponse response = calificacionService.obtenerPorRecomendacion(recomendacionId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Listar todas las calificaciones registradas",
            description = "Devuelve la totalidad de calificaciones emitidas por los estudiantes en la plataforma para evaluación y auditoría de la calidad de las respuestas RAG."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de calificaciones obtenido exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CalificacionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<CalificacionResponse>> listar() {
        return ResponseEntity.ok(calificacionService.listar());
    }
}
