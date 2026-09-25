package com.example.demo.curso.service.impl;

import com.example.demo.curso.dto.request.EstudianteRequest;
import com.example.demo.curso.model.Estudiante;
import com.example.demo.curso.model.Usuario;
import com.example.demo.curso.repository.EstudianteRepository;
import com.example.demo.curso.repository.UsuarioRepository;
import com.example.demo.curso.service.EstudianteService;
import com.example.demo.exception.BusinessRuleException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.util.PasswordUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class EstudianteServiceImpl extends CrudServiceImpl<Estudiante, String> implements EstudianteService {

    private static final Set<String> NIVELES_PERMITIDOS = Set.of("Principiante", "Intermedio", "Avanzado");

    private final EstudianteRepository estudianteRepository;
    private final UsuarioRepository usuarioRepository;

    public EstudianteServiceImpl(EstudianteRepository estudianteRepository, UsuarioRepository usuarioRepository) {
        super(estudianteRepository);
        this.estudianteRepository = estudianteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public Estudiante registrar(EstudianteRequest request) {
        validarNivelExperiencia(request.getNivelExperiencia());

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new BusinessRuleException("El correo electrónico es obligatorio.");
        }

        String email = request.getEmail().trim().toLowerCase();
        if (usuarioRepository.findByEmailIgnoreCase(email).isPresent()) {
            throw new BusinessRuleException("El correo ya está registrado.");
        }

        String id = (request.getId() != null && !request.getId().trim().isEmpty())
                ? request.getId().trim()
                : UUID.randomUUID().toString();

        String rawPassword = (request.getPassword() != null && !request.getPassword().trim().isEmpty())
                ? request.getPassword().trim()
                : "RutaIA2026!";

        Usuario usuario = new Usuario(id, email, PasswordUtils.sha256(rawPassword), "ESTUDIANTE");
        usuario = usuarioRepository.save(usuario);

        Estudiante estudiante = new Estudiante(
                id,
                request.getNombre().trim(),
                request.getNivelExperiencia().trim(),
                request.getAreaInteres().trim(),
                usuario
        );

        return estudianteRepository.save(estudiante);
    }

    @Override
    @Transactional
    public Estudiante actualizar(String id, EstudianteRequest request) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante", id));

        validarNivelExperiencia(request.getNivelExperiencia());

        estudiante.setNombre(request.getNombre().trim());
        estudiante.setNivelExperiencia(request.getNivelExperiencia().trim());
        estudiante.setAreaInteres(request.getAreaInteres().trim());

        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            String email = request.getEmail().trim().toLowerCase();
            Usuario usuario = usuarioRepository.findById(id).orElse(null);
            if (usuario != null && !email.equalsIgnoreCase(usuario.getEmail())) {
                if (usuarioRepository.findByEmailIgnoreCase(email).isPresent()) {
                    throw new BusinessRuleException("El correo ya está registrado.");
                }
                usuario.setEmail(email);
                usuarioRepository.save(usuario);
            }
        }

        return estudianteRepository.save(estudiante);
    }

    private void validarNivelExperiencia(String nivel) {
        if (nivel == null || !NIVELES_PERMITIDOS.contains(nivel.trim())) {
            throw new BusinessRuleException("El nivel de experiencia debe ser estrictamente: Principiante, Intermedio o Avanzado");
        }
    }
}
