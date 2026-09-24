package com.example.demo.curso.service;

import com.example.demo.curso.dto.request.ConsultaRequest;
import com.example.demo.curso.dto.response.ConsultaConResultadoResponse;
import com.example.demo.curso.dto.response.ConsultaResponse;
import com.example.demo.curso.dto.response.HistorialResponse;

import java.util.List;

public interface ConsultaService {

    ConsultaConResultadoResponse crearConsulta(ConsultaRequest request);

    ConsultaResponse obtenerPorId(String id);

    List<HistorialResponse> listarHistorial(String estudianteId);
}
