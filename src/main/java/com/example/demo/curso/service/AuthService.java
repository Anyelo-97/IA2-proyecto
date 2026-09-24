package com.example.demo.curso.service;

import com.example.demo.curso.dto.request.LoginRequest;
import com.example.demo.curso.dto.request.RegistroAdministradorRequest;
import com.example.demo.curso.dto.request.RegistroEstudianteRequest;
import com.example.demo.curso.dto.response.AuthResponse;
import com.example.demo.curso.dto.response.UsuarioResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse registrarEstudiante(RegistroEstudianteRequest request);
    AuthResponse registrarAdministrador(RegistroAdministradorRequest request);
    UsuarioResponse obtenerUsuario(String token);
}
