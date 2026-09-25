package com.example.demo.curso.controller;

import com.example.demo.curso.dto.request.CursoRequest;
import com.example.demo.curso.dto.response.CursoResponse;
import com.example.demo.curso.service.CursoService;
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

@Tag(name = "3. Catálogo y Cursos", description = "Gestión y consulta del catálogo institucional de cursos activos y sincronización vectorial.")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @Operation(
            summary = "Listar catálogo de cursos activos (con filtros opcionales de categoría y nivel)",
            description = "Devuelve el catálogo de cursos ofrecidos por la institución académica. Permite filtrar de forma opcional por categoría, nivel de dificultad y estado activo/inactivo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Catálogo de cursos obtenido exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping({"/cursos", "/catalogo"})
    public ResponseEntity<List<CursoResponse>> listarCursos(
            @Parameter(description = "Filtrar por identificador de categoría (opcional)", example = "cat-001")
            @RequestParam(required = false) String categoriaId,
            @Parameter(description = "Filtrar por identificador de nivel de dificultad (opcional)", example = "niv-001")
            @RequestParam(required = false) String nivelId,
            @Parameter(description = "Filtrar por estado activo/inactivo (opcional)", example = "true")
            @RequestParam(required = false) Boolean estado,
            @Parameter(description = "Incluir cursos inactivos (uso administrativo)", hidden = true)
            @RequestParam(required = false) Boolean incluirInactivos) {

        if (categoriaId != null || nivelId != null) {
            return ResponseEntity.ok(cursoService.obtenerCatalogo(categoriaId, nivelId));
        }
        if (Boolean.TRUE.equals(incluirInactivos)) {
            return ResponseEntity.ok(cursoService.listarCursos(null));
        }
        return ResponseEntity.ok(cursoService.listarCursos(estado));
    }

    @Operation(
            summary = "Obtener detalle de curso por ID",
            description = "Recupera la ficha técnica completa de un curso específico a partir de su identificador UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Curso obtenido exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping("/cursos/{id}")
    public ResponseEntity<CursoResponse> obtenerCursoPorId(
            @Parameter(description = "Identificador único UUID del curso", example = "c0000001-0000-4000-8000-000000000001")
            @PathVariable String id) {
        return ResponseEntity.ok(cursoService.obtenerCursoPorId(id));
    }

    @Operation(
            summary = "Registrar nuevo curso en el catálogo (Administrativo)",
            description = "Operación administrativa para agregar un nuevo curso al catálogo de la institución. Sincroniza automáticamente los datos con el vector store en Qdrant vía n8n."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Curso creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o conflicto de identificador", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno al persistir el curso o sincronizar con Qdrant", content = @Content)
    })
    @PostMapping("/cursos")
    public ResponseEntity<CursoResponse> crearCurso(@Valid @RequestBody CursoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.crearCurso(request));
    }

    @Operation(
            summary = "Actualizar información de curso (Administrativo)",
            description = "Operación administrativa para modificar el contenido, duración, categoría o nivel de un curso existente y actualizar su vector embedding en Qdrant."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Curso actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CursoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PutMapping("/cursos/{id}")
    public ResponseEntity<CursoResponse> actualizarCurso(
            @Parameter(description = "Identificador único UUID del curso", example = "c0000001-0000-4000-8000-000000000001")
            @PathVariable String id,
            @Valid @RequestBody CursoRequest request) {
        return ResponseEntity.ok(cursoService.actualizarCurso(id, request));
    }

    @Operation(
            summary = "Desactivar curso del catálogo (Administrativo)",
            description = "Realiza una baja lógica (soft delete) del curso en la base de datos y desactiva su punto vectorial en Qdrant para excluirlo de futuras recomendaciones."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Curso desactivado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Curso no encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @PatchMapping("/cursos/{id}/desactivar")
    public ResponseEntity<Void> desactivarCurso(
            @Parameter(description = "Identificador único UUID del curso", example = "c0000001-0000-4000-8000-000000000001")
            @PathVariable String id) {
        cursoService.desactivarCurso(id);
        return ResponseEntity.noContent().build();
    }
}
