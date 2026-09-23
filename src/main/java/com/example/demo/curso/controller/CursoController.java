package com.example.demo.curso.controller;

import com.example.demo.curso.dto.request.CursoRequest;
import com.example.demo.curso.dto.response.CursoResponse;
import com.example.demo.curso.service.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Cursos")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @Operation(summary = "Listar cursos")
    @ApiResponse(responseCode = "200", description = "Lista de cursos obtenida exitosamente")
    @GetMapping("/cursos")
    public ResponseEntity<List<CursoResponse>> listarCursos(@RequestParam(required = false) Boolean estado) {
        return ResponseEntity.ok(cursoService.listarCursos(estado));
    }

    @Operation(summary = "Obtener curso por ID")
    @ApiResponse(responseCode = "200", description = "Curso obtenido exitosamente")
    @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    @GetMapping("/cursos/{id}")
    public ResponseEntity<CursoResponse> obtenerCursoPorId(@PathVariable String id) {
        return ResponseEntity.ok(cursoService.obtenerCursoPorId(id));
    }

    @Operation(summary = "Crear nuevo curso")
    @ApiResponse(responseCode = "201", description = "Curso creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    @PostMapping("/cursos")
    public ResponseEntity<CursoResponse> crearCurso(@Valid @RequestBody CursoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.crearCurso(request));
    }

    @Operation(summary = "Actualizar curso")
    @ApiResponse(responseCode = "200", description = "Curso actualizado exitosamente")
    @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    @PutMapping("/cursos/{id}")
    public ResponseEntity<CursoResponse> actualizarCurso(
            @PathVariable String id,
            @Valid @RequestBody CursoRequest request) {
        return ResponseEntity.ok(cursoService.actualizarCurso(id, request));
    }

    @Operation(summary = "Desactivar curso")
    @ApiResponse(responseCode = "204", description = "Curso desactivado exitosamente")
    @ApiResponse(responseCode = "404", description = "Curso no encontrado")
    @PatchMapping("/cursos/{id}/desactivar")
    public ResponseEntity<Void> desactivarCurso(@PathVariable String id) {
        cursoService.desactivarCurso(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener catálogo de cursos activos")
    @ApiResponse(responseCode = "200", description = "Catálogo obtenido exitosamente")
    @GetMapping("/catalogo")
    public ResponseEntity<List<CursoResponse>> obtenerCatalogo(
            @RequestParam(required = false) String categoriaId,
            @RequestParam(required = false) String nivelId) {
        return ResponseEntity.ok(cursoService.obtenerCatalogo(categoriaId, nivelId));
    }
}
