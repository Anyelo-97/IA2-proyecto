package com.example.demo.curso.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Respuesta de autenticación con el token JWT y los datos del usuario.")
public class AuthResponse {
    @Schema(description = "Token JWT para autorizar peticiones protegidas en Swagger y en la API.")
    private String token;

    @Schema(description = "Usuario autenticado.")
    private UsuarioResponse usuario;
}
