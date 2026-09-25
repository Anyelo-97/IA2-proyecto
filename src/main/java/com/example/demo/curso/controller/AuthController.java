package com.example.demo.curso.controller;

import com.example.demo.curso.dto.request.LoginRequest;
import com.example.demo.curso.dto.request.RegistroAdministradorRequest;
import com.example.demo.curso.dto.request.RegistroEstudianteRequest;
import com.example.demo.curso.dto.response.AuthResponse;
import com.example.demo.curso.dto.response.UsuarioResponse;
import com.example.demo.curso.service.AuthService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.example.demo.curso.model.Usuario;
import com.example.demo.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "0. Autenticación y Cuentas", description = "Inicio de sesión, registro de cuentas de estudiantes/administradores y consulta de perfil actual.")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica a un estudiante o administrador mediante su correo electrónico (o identificador) y contraseña, devolviendo los datos de la sesión y el token de acceso."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
            @ApiResponse(responseCode = "400", description = "Credenciales inválidas o datos incompletos")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(
            summary = "Registro público de estudiante",
            description = "Permite a un nuevo estudiante registrar su cuenta institucional especificando su nombre, correo, contraseña, nivel de experiencia y área de interés."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estudiante registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de registro inválidos o correo ya registrado")
    })
    @PostMapping("/register/estudiante")
    public ResponseEntity<AuthResponse> registrarEstudiante(@Valid @RequestBody RegistroEstudianteRequest request) {
        return ResponseEntity.ok(authService.registrarEstudiante(request));
    }

    @Operation(
            summary = "Registro de administrador (con código institucional)",
            description = "Registra una nueva cuenta de administrador previa validación de la clave o código institucional."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Administrador registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Código institucional incorrecto o correo ya registrado")
    })
    @PostMapping("/register/admin")
    public ResponseEntity<AuthResponse> registrarAdministrador(@Valid @RequestBody RegistroAdministradorRequest request) {
        return ResponseEntity.ok(authService.registrarAdministrador(request));
    }

    @Operation(
            summary = "Obtener perfil del usuario autenticado",
            description = "Devuelve los datos del perfil del usuario actualmente autenticado mediante el contexto de seguridad o encabezado Bearer."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil recuperado exitosamente"),
            @ApiResponse(responseCode = "401", description = "Token no proporcionado o inválido")
    })
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> me(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return ResponseEntity.ok(authService.obtenerUsuario(authorization.substring(7)));
        }
        if (userDetails != null && userDetails.getUsuario() != null) {
            Usuario u = userDetails.getUsuario();
            return ResponseEntity.ok(new UsuarioResponse(u.getId(), u.getEmail(), u.getEmail(), u.getRol()));
        }
        return ResponseEntity.status(401).build();
    }
}
