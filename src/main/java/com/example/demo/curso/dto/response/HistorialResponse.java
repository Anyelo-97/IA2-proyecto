package com.example.demo.curso.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistorialResponse {
    private String consultaId;
    private String pregunta;
    private LocalDateTime fecha;
    private String estado;
    private String resumen;
    private List<String> cursosRecomendados;
}
