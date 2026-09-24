package com.example.demo.curso.controller;

import com.example.demo.curso.dto.request.CalificacionRequest;
import com.example.demo.curso.dto.response.CalificacionResponse;
import com.example.demo.curso.service.CalificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
public class CalificacionController {

    private final CalificacionService calificacionService;

    @PostMapping
    public ResponseEntity<CalificacionResponse> calificar(@Valid @RequestBody CalificacionRequest request) {
        CalificacionResponse response = calificacionService.calificar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{recomendacionId}")
    public ResponseEntity<CalificacionResponse> obtenerPorRecomendacion(@PathVariable String recomendacionId) {
        CalificacionResponse response = calificacionService.obtenerPorRecomendacion(recomendacionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CalificacionResponse>> listar() {
        return ResponseEntity.ok(calificacionService.listar());
    }
}
