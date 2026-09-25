package com.example.demo.curso.controller;

import com.example.demo.curso.dto.request.EstudianteRequest;
import com.example.demo.curso.dto.response.EstudianteResponse;
import com.example.demo.curso.mapper.EstudianteMapper;
import com.example.demo.curso.service.EstudianteService;
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

@Tag(name = "4. Estudiantes", description = "Registro y consulta de perfiles de estudiantes con validación estricta de experiencia y correo único.")
@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final EstudianteService service;
    private final EstudianteMapper mapper;

    @Operation(
            summary = "Listar todos los estudiantes registrados",
            description = "Devuelve el listado completo de estudiantes registrados en la plataforma académica."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de estudiantes obtenido exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstudianteResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<EstudianteResponse>> listar() {
        return ResponseEntity.ok(service.listar().stream().map(mapper::entityToDto).toList());
    }

    @Operation(
            summary = "Obtener perfil de estudiante por ID",
            description = "Recupera el perfil académico y datos de contacto de un estudiante específico mediante su identificador único."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil de estudiante obtenido exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstudianteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EstudianteResponse> buscar(
            @Parameter(description = "Identificador único del estudiante", example = "est-001") @PathVariable String id) {
        return service.buscarPorId(id).map(mapper::entityToDto)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Registrar nuevo estudiante",
            description = "Registra un nuevo estudiante en el sistema educativo. Valida estrictamente que el correo electrónico no esté duplicado, que el nivel de experiencia corresponda a ('Principiante', 'Intermedio' o 'Avanzado') y que el área técnica de interés no esté vacía."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Estudiante registrado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EstudianteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos, correo ya registrado o nivel de experiencia no permitido", content = @Content)
    })
    @PostMapping
    public ResponseEntity<EstudianteResponse> crear(@Valid @RequestBody EstudianteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.entityToDto(service.registrar(request)));
    }

    @Operation(
            summary = "Actualizar información de estudiante (Administrativo)",
            description = "Actualiza los datos del perfil del estudiante especificado por su identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estudiante actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<EstudianteResponse> actualizar(
            @Parameter(description = "Identificador del estudiante", example = "est-001") @PathVariable String id,
            @Valid @RequestBody EstudianteRequest request) {
        return ResponseEntity.ok(mapper.entityToDto(service.actualizar(id, request)));
    }

    @Operation(
            summary = "Eliminar estudiante (Administrativo)",
            description = "Elimina un estudiante registrado de la plataforma académica."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Estudiante eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "Identificador del estudiante", example = "est-001") @PathVariable String id) {
        if (service.buscarPorId(id).isEmpty()) return ResponseEntity.notFound().build();
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
