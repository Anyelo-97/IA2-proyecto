package com.example.demo.curso.service;

import com.example.demo.curso.model.Usuario;
import com.example.demo.curso.dto.request.UsuarioRequest;
import com.example.demo.curso.dto.response.UsuarioResponse;

import java.util.List;

public interface UsuarioService extends CrudService<Usuario, String> {
    UsuarioResponse crear(UsuarioRequest request);
    UsuarioResponse obtenerPorId(String id);
    List<UsuarioResponse> listarResponses();
}
