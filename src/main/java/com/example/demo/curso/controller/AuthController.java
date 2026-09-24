package com.example.demo.curso.controller;

import com.example.demo.curso.dto.request.LoginRequest;
import com.example.demo.curso.dto.request.RegistroAdministradorRequest;
import com.example.demo.curso.dto.request.RegistroEstudianteRequest;
import com.example.demo.curso.dto.response.AuthResponse;
import com.example.demo.curso.dto.response.UsuarioResponse;
import com.example.demo.curso.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register/estudiante")
    public ResponseEntity<AuthResponse> registrarEstudiante(@Valid @RequestBody RegistroEstudianteRequest request) {
        return ResponseEntity.ok(authService.registrarEstudiante(request));
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponse> registrarAdministrador(@Valid @RequestBody RegistroAdministradorRequest request) {
        return ResponseEntity.ok(authService.registrarAdministrador(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> me(@RequestHeader(value = "Authorization", required = false) String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(authService.obtenerUsuario(authorization.substring(7)));
    }
}
