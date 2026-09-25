package com.example.demo.curso.controller;

import com.example.demo.curso.dto.request.AdministradorRequest;
import com.example.demo.curso.dto.response.AdministradorResponse;
import com.example.demo.curso.mapper.AdministradorMapper;
import com.example.demo.curso.service.AdministradorService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/api/administradores")
@RequiredArgsConstructor
public class AdministradorController {
    private final AdministradorService service;
    private final AdministradorMapper mapper;

    @GetMapping
    public List<AdministradorResponse> listar() { return service.listar().stream().map(mapper::entityToDto).toList(); }

    @GetMapping("/{id}")
    public ResponseEntity<AdministradorResponse> buscar(@PathVariable String id) {
        return service.buscarPorId(id).map(mapper::entityToDto).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AdministradorResponse> crear(@Valid @RequestBody AdministradorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.entityToDto(service.guardar(mapper.requestToEntity(request))));
    }
}
