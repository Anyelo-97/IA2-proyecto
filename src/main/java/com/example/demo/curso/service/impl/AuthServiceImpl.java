package com.example.demo.curso.service.impl;

import com.example.demo.curso.dto.request.LoginRequest;
import com.example.demo.curso.dto.request.RegistroAdministradorRequest;
import com.example.demo.curso.dto.request.RegistroEstudianteRequest;
import com.example.demo.curso.dto.response.AuthResponse;
import com.example.demo.curso.dto.response.UsuarioResponse;
import com.example.demo.curso.model.Administrador;
import com.example.demo.curso.model.Estudiante;
import com.example.demo.curso.model.Usuario;
import com.example.demo.curso.repository.AdministradorRepository;
import com.example.demo.curso.repository.EstudianteRepository;
import com.example.demo.curso.repository.UsuarioRepository;
import com.example.demo.curso.service.AuthService;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final EstudianteRepository estudianteRepository;
    private final AdministradorRepository administradorRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${rutaia.auth.admin-registration-code:}")
    private String adminRegistrationCode;

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String identificador = request.getIdentificador() == null
                ? ""
                : request.getIdentificador().trim();
        String password = request.getPassword() == null
                ? ""
                : request.getPassword();
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(identificador)
                .orElseGet(() -> usuarioRepository.findById(identificador).orElse(null));
        if (usuario == null || !passwordEncoder.matches(password, usuario.getPassword())) {
            throw new BusinessRuleException("Credenciales incorrectas.");
        }
        String nombre = obtenerNombreUsuario(usuario);
        String token = jwtService.generateToken(usuario.getId(), usuario.getEmail(), usuario.getRol(), nombre);
        return new AuthResponse(token, toResponse(usuario, nombre));
    }

    @Override
    @Transactional
    public AuthResponse registrarEstudiante(RegistroEstudianteRequest request) {
        ensureEmailAvailable(request.getEmail());
        String id = UUID.randomUUID().toString();
        Usuario usuario = new Usuario(id, request.getEmail().trim().toLowerCase(), passwordEncoder.encode(request.getPassword()), "ESTUDIANTE");
        usuario = usuarioRepository.save(usuario);
        estudianteRepository.save(new Estudiante(id, request.getNombre().trim(), request.getNivelExperiencia(), request.getAreaInteres().trim(), usuario));
        String token = jwtService.generateToken(usuario.getId(), usuario.getEmail(), usuario.getRol(), request.getNombre().trim());
        return new AuthResponse(token, toResponse(usuario, request.getNombre().trim()));
    }

    @Override
    @Transactional
    public AuthResponse registrarAdministrador(RegistroAdministradorRequest request) {
        if (adminRegistrationCode.isBlank() || !adminRegistrationCode.equals(request.getCodigoAutorizacion())) {
            throw new BusinessRuleException("Código de autorización inválido.");
        }
        ensureEmailAvailable(request.getEmail());
        String id = UUID.randomUUID().toString();
        Usuario usuario = new Usuario(id, request.getEmail().trim().toLowerCase(), passwordEncoder.encode(request.getPassword()), "ADMINISTRADOR");
        usuario = usuarioRepository.save(usuario);
        administradorRepository.save(new Administrador(id, request.getNombre().trim(), usuario));
        String token = jwtService.generateToken(usuario.getId(), usuario.getEmail(), usuario.getRol(), request.getNombre().trim());
        return new AuthResponse(token, toResponse(usuario, request.getNombre().trim()));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerUsuario(String token) {
        try {
            String userId = jwtService.extractUserId(token);
            if (userId == null || userId.isBlank()) {
                userId = jwtService.extractEmail(token);
            }
            if (userId == null || userId.isBlank()) {
                throw new BusinessRuleException("Sesión inválida.");
            }
            final String searchId = userId;
            Usuario usuario = usuarioRepository.findById(searchId)
                    .or(() -> usuarioRepository.findByEmailIgnoreCase(searchId))
                    .orElseThrow(() -> new BusinessRuleException("Sesión inválida."));
            return toResponse(usuario);
        } catch (Exception e) {
            throw new BusinessRuleException("Sesión inválida.");
        }
    }

    private void ensureEmailAvailable(String email) {
        if (usuarioRepository.findByEmailIgnoreCase(email.trim()).isPresent()) {
            throw new BusinessRuleException("El correo ya está registrado.");
        }
    }

    private String obtenerNombreUsuario(Usuario usuario) {
        String nombre = usuario.getEmail();
        if ("ESTUDIANTE".equals(usuario.getRol())) {
            nombre = estudianteRepository.findById(usuario.getId()).map(Estudiante::getNombre).orElse(nombre);
        } else if ("ADMINISTRADOR".equals(usuario.getRol())) {
            nombre = administradorRepository.findById(usuario.getId()).map(Administrador::getNombre).orElse(nombre);
        }
        return nombre;
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        String nombre = obtenerNombreUsuario(usuario);
        return new UsuarioResponse(usuario.getId(), nombre, usuario.getEmail(), usuario.getRol());
    }

    private UsuarioResponse toResponse(Usuario usuario, String nombre) {
        return new UsuarioResponse(usuario.getId(), nombre, usuario.getEmail(), usuario.getRol());
    }
}
