package com.example.demo.curso.controller;

import com.example.demo.curso.dto.response.NivelDificultadResponse;
import com.example.demo.curso.mapper.NivelDificultadMapper;
import com.example.demo.curso.service.NivelDificultadService;
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

import java.util.List;

@Tag(name = "6. Catálogos de Referencia", description = "Catálogos institucionales fijos para filtrado por categorías temáticas y niveles de dificultad.")
@RestController
@RequestMapping({"/api/niveles-dificultad", "/api/niveles"})
@RequiredArgsConstructor
public class NivelDificultadController {

    private final NivelDificultadService service;
    private final NivelDificultadMapper mapper;

    @Operation(
            summary = "Listar niveles de dificultad para filtrado",
            description = "Devuelve el catálogo institucional fijo de niveles de dificultad (Básico, Intermedio, Avanzado) utilizado para clasificar los cursos y filtrar resultados en el frontend."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Catálogo de niveles de dificultad obtenido exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = NivelDificultadResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al consultar el catálogo", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<NivelDificultadResponse>> listar() {
        return ResponseEntity.ok(service.listar().stream().map(mapper::entityToDto).toList());
    }
}
