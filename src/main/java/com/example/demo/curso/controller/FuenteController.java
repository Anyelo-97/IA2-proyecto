package com.example.demo.curso.controller;

import com.example.demo.curso.dto.request.FuenteRequest;
import com.example.demo.curso.dto.response.FuenteResponse;
import com.example.demo.curso.mapper.FuenteMapper;
import com.example.demo.curso.service.FuenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fuentes")
@RequiredArgsConstructor
public class FuenteController {
    private final FuenteService service;
    private final FuenteMapper mapper;

    @GetMapping
    public List<FuenteResponse> listar() { return service.listar().stream().map(mapper::entityToDto).toList(); }

    @PostMapping
    public ResponseEntity<FuenteResponse> crear(@Valid @RequestBody FuenteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.entityToDto(service.guardar(mapper.requestToEntity(request))));
    }
}
