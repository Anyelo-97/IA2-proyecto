package com.example.demo.curso.service;

import com.example.demo.curso.model.Consulta;
import com.example.demo.curso.dto.request.ConsultaRequest;
import com.example.demo.curso.dto.response.ConsultaResponse;
import java.util.List;

public interface ConsultaService extends CrudService<Consulta, String> {
    ConsultaResponse crear(ConsultaRequest request);
    ConsultaResponse obtenerPorId(String id);
    List<ConsultaResponse> listarResponses();
}
