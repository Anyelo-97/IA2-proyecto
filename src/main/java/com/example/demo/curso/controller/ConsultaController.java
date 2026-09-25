package com.example.demo.curso.controller;

import com.example.demo.curso.dto.request.ConsultaRequest;
import com.example.demo.curso.dto.response.ConsultaConResultadoResponse;
import com.example.demo.curso.dto.response.ConsultaResponse;
import com.example.demo.curso.dto.response.HistorialResponse;
import com.example.demo.curso.service.ConsultaService;
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

@Tag(name = "1. Consultas Inteligentes (RAG)", description = "Procesamiento de consultas en lenguaje natural mediante pipeline RAG sincrónico e historial académico.")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @Operation(
            summary = "Procesar consulta académica en lenguaje natural mediante RAG",
            description = "Procesa una consulta en lenguaje natural realizada por un estudiante utilizando un flujo sincrónico RAG (Retrieval-Augmented Generation): "
                    + "Spring Boot -> n8n -> Qdrant (búsqueda semántica por similitud coseno de 1536 dimensiones) -> OpenRouter (generación de respuesta contextualizada con LLM) "
                    + "-> Recomendación persistida en MySQL junto con las fuentes de cursos utilizadas y sus puntuaciones de similitud."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Consulta procesada exitosamente; recomendación y fuentes persistidas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConsultaConResultadoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Pregunta vacía o solicitud inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado con el identificador proporcionado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno en el pipeline RAG o en la base de datos", content = @Content)
    })
    @PostMapping("/consultas")
    public ResponseEntity<ConsultaConResultadoResponse> crearConsulta(
            @Valid @RequestBody ConsultaRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null && userDetails.getId() != null) {
            request.setEstudianteId(userDetails.getId());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.crearConsulta(request));
    }

    @Operation(
            summary = "Obtener detalle de consulta y recomendación por ID",
            description = "Recupera la información básica de una consulta registrada en el sistema por su identificador único."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta encontrada exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConsultaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Consulta no encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/consultas/{id}")
    public ResponseEntity<ConsultaResponse> obtenerPorId(
            @Parameter(description = "Identificador único de la consulta", example = "con-001") @PathVariable String id) {
        return ResponseEntity.ok(consultaService.obtenerPorId(id));
    }

    @Operation(
            summary = "Obtener historial completo de consultas y fuentes del estudiante autenticado",
            description = "Recupera el historial de consultas del estudiante autenticado extrayendo su identificador directamente del token JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = HistorialResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/consultas/historial")
    public ResponseEntity<List<HistorialResponse>> listarMiHistorial(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String estudianteId = (userDetails != null) ? userDetails.getId() : null;
        if (estudianteId == null) {
            throw new org.springframework.security.access.AccessDeniedException("Debe estar autenticado para consultar su historial.");
        }
        return ResponseEntity.ok(consultaService.listarHistorial(estudianteId));
    }

    @Operation(
            summary = "Obtener historial completo de consultas y fuentes de un estudiante",
            description = "Recupera el historial cronológico completo de consultas realizadas por un estudiante. Los estudiantes solo pueden consultar su propio historial; los administradores pueden consultar el historial de cualquier estudiante."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = HistorialResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: no puede consultar el historial de otro estudiante", content = @Content),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping({"/consultas/historial/{estudianteId}", "/estudiantes/{estudianteId}/historial", "/consultas/estudiante/{estudianteId}/historial"})
    public ResponseEntity<List<HistorialResponse>> listarHistorial(
            @Parameter(description = "Identificador del estudiante", example = "est-001") @PathVariable String estudianteId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null && userDetails.getId() != null && !esAdmin(userDetails) && !userDetails.getId().equalsIgnoreCase(estudianteId)) {
            throw new org.springframework.security.access.AccessDeniedException("No tiene permisos para consultar el historial de otro estudiante.");
        }
        return ResponseEntity.ok(consultaService.listarHistorial(estudianteId));
    }

    private boolean esAdmin(CustomUserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMINISTRADOR".equals(a.getAuthority()) || "ROLE_ADMIN".equals(a.getAuthority()));
    }
}
