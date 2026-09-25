package com.example.demo.curso.controller;

import com.example.demo.curso.dto.request.CategoriaRequest;
import com.example.demo.curso.dto.response.CategoriaResponse;
import com.example.demo.curso.mapper.CategoriaMapper;
import com.example.demo.curso.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "6. Catálogos de Referencia", description = "Catálogos institucionales fijos para filtrado por categorías temáticas y niveles de dificultad.")
@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;
    private final CategoriaMapper mapper;

    @Operation(
            summary = "Listar categorías disponibles para filtrado",
            description = "Devuelve el catálogo de categorías académicas disponibles para la clasificación y filtrado de cursos en la plataforma."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriaResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listar() {
        return ResponseEntity.ok(service.listar().stream().map(mapper::entityToDto).toList());
    }

    @Operation(
            summary = "Obtener categoría por ID",
            description = "Recupera la información detallada de una categoría específica por su identificador único."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> buscar(
            @Parameter(description = "Identificador de la categoría", example = "cat-001") @PathVariable String id) {
        return service.buscarPorId(id).map(mapper::entityToDto)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Registrar nueva categoría (Administrativo)",
            description = "Crea una nueva categoría temática en el catálogo institucional. Requiere privilegios administrativos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de categoría inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CategoriaResponse> crear(@Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.entityToDto(service.guardar(mapper.requestToEntity(request))));
    }

    @Operation(
            summary = "Actualizar categoría (Administrativo)",
            description = "Actualiza el nombre de una categoría existente. Requiere privilegios administrativos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> actualizar(
            @Parameter(description = "Identificador de la categoría", example = "cat-001") @PathVariable String id,
            @Valid @RequestBody CategoriaRequest request) {
        if (service.buscarPorId(id).isEmpty()) return ResponseEntity.notFound().build();
        request.setId(id);
        return ResponseEntity.ok(mapper.entityToDto(service.guardar(mapper.requestToEntity(request))));
    }

    @Operation(
            summary = "Eliminar categoría (Administrativo)",
            description = "Elimina una categoría del catálogo institucional si no tiene cursos asociados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Identificador de la categoría", example = "cat-001") @PathVariable String id) {
        if (service.buscarPorId(id).isEmpty()) return ResponseEntity.notFound().build();
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
