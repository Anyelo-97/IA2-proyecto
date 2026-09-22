package com.example.demo.curso.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultaConResultadoResponse {
    private String consultaId;
    private String pregunta;
    private String estado;
    private String respuesta;
    private List<FuenteResponse> fuentes;
}
