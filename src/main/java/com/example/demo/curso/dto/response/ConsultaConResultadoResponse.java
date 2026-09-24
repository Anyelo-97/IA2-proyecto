package com.example.demo.curso.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaConResultadoResponse {
    private String consultaId;
    private String pregunta;
    private String estado;
    private String respuesta;
    private List<FuenteResponse> fuentes;
}
