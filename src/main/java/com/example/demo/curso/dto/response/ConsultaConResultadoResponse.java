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
    private String recomendacionId;
    private String pregunta;
    private String estado;
    private String respuesta;
    private List<FuenteResponse> fuentes;

    public ConsultaConResultadoResponse(String consultaId, String pregunta, String estado, String respuesta, List<FuenteResponse> fuentes) {
        this.consultaId = consultaId;
        this.pregunta = pregunta;
        this.estado = estado;
        this.respuesta = respuesta;
        this.fuentes = fuentes;
    }
}
