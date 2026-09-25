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
import com.example.demo.util.PasswordUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UsuarioRepository usuarioRepository;
    private final EstudianteRepository estudianteRepository;
    private final AdministradorRepository administradorRepository;

    @Value("${rutaia.auth.admin-registration-code:}")
    private String adminRegistrationCode;

    private final Map<String, String> sessions = new ConcurrentHashMap<>();

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
        if (usuario == null || !passwordMatches(password, usuario.getPassword())) {
            throw new BusinessRuleException("Credenciales incorrectas.");
        }
        return createSession(usuario);
    }

    @Override
    @Transactional
    public AuthResponse registrarEstudiante(RegistroEstudianteRequest request) {
        ensureEmailAvailable(request.getEmail());
        String id = UUID.randomUUID().toString();
        Usuario usuario = new Usuario(id, request.getEmail().trim().toLowerCase(), PasswordUtils.sha256(request.getPassword()), "ESTUDIANTE");
        usuario = usuarioRepository.save(usuario);
        estudianteRepository.save(new Estudiante(id, request.getNombre().trim(), request.getNivelExperiencia(), request.getAreaInteres().trim(), usuario));
        return createSession(usuario);
    }

    @Override
    @Transactional
    public AuthResponse registrarAdministrador(RegistroAdministradorRequest request) {
        if (adminRegistrationCode.isBlank() || !adminRegistrationCode.equals(request.getCodigoAutorizacion())) {
            throw new BusinessRuleException("Código de autorización inválido.");
        }
        ensureEmailAvailable(request.getEmail());
        String id = UUID.randomUUID().toString();
        Usuario usuario = new Usuario(id, request.getEmail().trim().toLowerCase(), PasswordUtils.sha256(request.getPassword()), "ADMINISTRADOR");
        usuario = usuarioRepository.save(usuario);
        administradorRepository.save(new Administrador(id, request.getNombre().trim(), usuario));
        return createSession(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerUsuario(String token) {
        String id = sessions.get(token);
        if (id == null) throw new BusinessRuleException("Sesión inválida.");
        return toResponse(usuarioRepository.findById(id).orElseThrow(() -> new BusinessRuleException("Sesión inválida.")));
    }

    private void ensureEmailAvailable(String email) {
        if (usuarioRepository.findByEmailIgnoreCase(email.trim()).isPresent()) {
            throw new BusinessRuleException("El correo ya está registrado.");
        }
    }

    private AuthResponse createSession(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, usuario.getId());
        return new AuthResponse(token, toResponse(usuario));
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        String nombre = usuario.getEmail();
        if ("ESTUDIANTE".equals(usuario.getRol())) {
            nombre = estudianteRepository.findById(usuario.getId()).map(Estudiante::getNombre).orElse(nombre);
        } else if ("ADMINISTRADOR".equals(usuario.getRol())) {
            nombre = administradorRepository.findById(usuario.getId()).map(Administrador::getNombre).orElse(nombre);
        }
        return new UsuarioResponse(usuario.getId(), nombre, usuario.getEmail(), usuario.getRol());
    }

    private boolean passwordMatches(String raw, String stored) {
        if (stored == null) {
            return false;
        }
        String normalizedStored = stored.trim();
        String hashedRaw = PasswordUtils.sha256(raw);
        return normalizedStored.equalsIgnoreCase(hashedRaw) || normalizedStored.equals(raw);
    }
}
