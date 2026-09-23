package com.example.demo.curso.service;

import com.example.demo.curso.model.Administrador;
import com.example.demo.curso.dto.request.AdministradorRequest;
import com.example.demo.curso.dto.response.AdministradorResponse;
import java.util.List;

public interface AdministradorService extends CrudService<Administrador, String> {
    AdministradorResponse crear(AdministradorRequest request);
    AdministradorResponse obtenerPorId(String id);
    List<AdministradorResponse> listarResponses();
}
