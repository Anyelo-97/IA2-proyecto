package com.example.demo.curso.controller;

import com.example.demo.curso.dto.request.ConsultaRequest;
import com.example.demo.curso.dto.response.ConsultaConResultadoResponse;
import com.example.demo.curso.dto.response.ConsultaResponse;
import com.example.demo.curso.dto.response.HistorialResponse;
import com.example.demo.curso.service.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Consultas")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @Operation(summary = "Crear nueva consulta", description = "Registra una consulta de estudiante y devuelve el resultado con sus fuentes")
    @ApiResponse(responseCode = "201", description = "Consulta creada exitosamente")
    @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    @PostMapping("/consultas")
    public ResponseEntity<ConsultaConResultadoResponse> crearConsulta(@Valid @RequestBody ConsultaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.crearConsulta(request));
    }

    @Operation(summary = "Obtener consulta por ID", description = "Obtiene los datos básicos de una consulta por su identificador")
    @ApiResponse(responseCode = "200", description = "Consulta obtenida exitosamente")
    @ApiResponse(responseCode = "404", description = "Consulta no encontrada")
    @GetMapping("/consultas/{id}")
    public ResponseEntity<ConsultaResponse> obtenerPorId(@PathVariable String id) {
        return ResponseEntity.ok(consultaService.obtenerPorId(id));
    }

    @Operation(summary = "Historial de consultas de un estudiante", description = "Lista el historial de consultas de un estudiante ordenado cronológicamente de forma descendente")
    @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente")
    @GetMapping({"/estudiantes/{estudianteId}/historial", "/consultas/estudiante/{estudianteId}/historial"})
    public ResponseEntity<List<HistorialResponse>> listarHistorial(@PathVariable String estudianteId) {
        return ResponseEntity.ok(consultaService.listarHistorial(estudianteId));
    }
}
